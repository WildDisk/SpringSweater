package com.example.springsweater.service

import com.example.springsweater.domain.Role
import com.example.springsweater.domain.User
import org.springframework.stereotype.Service

import com.example.springsweater.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.util.StringUtils
import java.util.*

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
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var mailSender: MailSender

    /**
     * Поиск имеющихся пользователей. Если есть,
     * отдаёт на аутентификацию
     *
     * @throws UsernameNotFoundException
     * @param username
     * @return UserDetails
     */
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails? = username?.let { userRepository.findByUsername(it) }

    /**
     * Добавление пользователя и отправка сообщения для подтверждения
     * аутентификации на почту
     *
     * @param user
     */
    fun addUser(user: User): Boolean {
        val userCheck = userRepository.findByUsername(user.username)
        return when {
            userCheck != null -> false
            else -> {
                user.isActive = true
                user.roles = mutableSetOf(Role.USER)
                user.activationCode = UUID.randomUUID().toString()
                userRepository.save(user)
                when {
                    !StringUtils.isEmpty(user.email) -> {
                        val message: String = """
                            Hello, ${user.username}
                            Welcome to SpringSweater. Pleas, visit next link: http://localhost:8080/active/${user.activationCode}
                        """.trimIndent()
                        mailSender.send(user.email, "Activation code", message)
                    }
                }
                true
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
            user.activationCode = null
            userRepository.save(user)
            true
        }
    }
}