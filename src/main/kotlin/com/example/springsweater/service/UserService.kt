package com.example.springsweater.service

import com.example.springsweater.domain.Role
import com.example.springsweater.domain.User
import com.example.springsweater.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*
import java.util.stream.Collectors

/**
 * Сервис отвечающий за аутентификацию пользователей и
 * генерацию автоматического сообщения для отправки
 * по email пользователя для подтверждения регистрации
 *
 * @project SpringSweater
 * @author WildDisk
 */
@Service
class UserService : UserDetailsService {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var mailSender: MailSender
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    /**
     * Поиск имеющихся пользователей. Если есть,
     * отдаёт на аутентификацию
     *
     * @throws UsernameNotFoundException
     * @param username
     * @return UserDetails
     */
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails? = username?.let {
        userRepository.findByUsername(it)
    } ?: throw UsernameNotFoundException("User not found!")

    /**
     * Создание пользователя
     *
     * @param user
     */
    fun addUser(user: User): Boolean {
        val userCheck = user.username?.let { userRepository.findByUsername(it) }
        return when {
            userCheck != null -> false
            else -> {
                user.isActive = false
                user.roles = mutableSetOf(Role.USER)
                user.activationCode = UUID.randomUUID().toString()
                user.setPassword(passwordEncoder.encode(user.password))
                userRepository.save(user)
                sendMessage(user)
                true
            }
        }
    }

    /**
     * Отправка сообщения пользователю для подтверждения регистрации
     *
     * @param user
     */
    private fun sendMessage(user: User) {
        when {
            !StringUtils.isEmpty(user.email) -> {
                val message: String = """
                            Hello, ${user.username}
                            Welcome to SpringSweater. Pleas, visit next link: http://localhost:8080/activate/${user.activationCode}
                        """.trimIndent()
                mailSender.send(user.email, "Activation code", message)
            }
        }
    }

    /**
     * Активация пользователя при переходе по ссылке с кодом из письма
     *
     * @param code
     */
    fun activateUser(code: String): Boolean = when (val user: User? = userRepository.findByActivationCode(code)) {
        null -> false
        else -> {
            user.isActive = true
            user.activationCode = null
            userRepository.save(user)
            true
        }
    }

    /**
     * Переопределяем функцию из userRepository.findAll() для вызова
     */
    fun findAll(): List<User> = userRepository.findAll()

    /**
     * Сохранение изменений профиля
     */
    fun saveUser(user: User, username: String, form: Map<String, String>) {
        user.setUsername(username)
        val roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet())
        user.roles.clear()
        form.forEach {
            if (roles.contains(it.key)) user.roles.add(Role.valueOf(it.key))
        }
        userRepository.save(user)
    }

    /**
     * Изменение профился
     *
     * @param user смена имени пользователя
     * @param password смена пароля пользователя
     * @param email смена email полььзователя
     */
    fun updateProfile(user: User, password: String, email: String) {
        val userEmail = user.email
        val isEmailChanged: Boolean = (email != userEmail) || (userEmail != email)
        when {
            isEmailChanged -> {
                user.email = email
                when {
                    !StringUtils.isEmpty(email) -> user.activationCode = UUID.randomUUID().toString()
                }
            }
        }
        when {
            !StringUtils.isEmpty(password) -> user.setPassword(passwordEncoder.encode(password))
        }
        userRepository.save(user)
        when {
            isEmailChanged -> sendMessage(user)
        }
    }

    /**
     * Подписаться на канал
     */
    fun subscribe(currentUser: User, user: User) {
        user.subscribers?.add(currentUser)
        userRepository.save(user)
    }

    /**
     * Отписаться от канала
     */
    fun unsubscribe(currentUser: User, user: User) {
        user.subscribers?.remove(currentUser)
        userRepository.save(user)
    }
}