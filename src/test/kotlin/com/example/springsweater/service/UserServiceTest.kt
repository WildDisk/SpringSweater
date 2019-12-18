package com.example.springsweater.service

import com.example.springsweater.domain.Role
import com.example.springsweater.domain.User
import com.example.springsweater.repository.UserRepository
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.internal.matchers.Equals
import org.mockito.internal.progress.ThreadSafeMockingProgress
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

/**
 * Тесты для [UserService]
 *
 * @author WildDisk on 12.12.2019
 * @project SpringSweater
 */
@SpringBootTest
internal class UserServiceTest {
    @Autowired
    private lateinit var userService: UserService

    @MockBean
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var mailSender: MailSender

    @Mock
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    /**
     * Добавление нового пользователя [UserService.addUser]
     */
    @Test
    fun addUser() {
        val user = User()
        user.email = "kekos53995@4tmail.com"
        val isUserCreated: Boolean = userService.addUser(user)
        assertTrue(isUserCreated)
        assertNotNull(user.activationCode)
        assertTrue(CoreMatchers.`is`(user.roles).matches(Collections.singleton(Role.USER)))

        Mockito.verify(userRepository, Mockito.times(1)).save(user)
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                        any(user.email),
                        any(),
                        any()
                )
    }

    /**
     * Тест на проверку совпадения username при создании нового
     * пользователя [UserService.addUser]
     * Если пользователь с таким именем существует то false
     */
    @Test
    fun addUserFailTest() {
        val user = User()
        user.setUsername("John")
        Mockito.doReturn(User())
                .`when`(userRepository)
                .findByUsername("John")
        val isUserCreated: Boolean = userService.addUser(user)
        assertFalse(isUserCreated)
        Mockito.verify(userRepository, Mockito.times(0))
                .save(ArgumentMatchers.any(User::class.java))
        Mockito.verify(mailSender, Mockito.times(0))
                .send(
                        any(),
                        any(),
                        any()
                )
    }

    /**
     * Активация учётной записи пользователя [UserService.activateUser]
     */
    @Test
    fun activateUser() {
        val user = User()
        user.activationCode = "bingo!"
        Mockito.doReturn(user)
                .`when`(userRepository)
                .findByActivationCode("activate")
        val isUserActivate: Boolean = userService.activateUser("activate")
        assertTrue(isUserActivate)
        assertNull(user.activationCode)
        Mockito.verify(userRepository, Mockito.times(1)).save(user)
    }

    /**
     * Учетная запись пользователя не активирована [UserService.activateUser]
     */
    @Test
    fun activateUserFailTest() {
        val isUserActivated = userService.activateUser("activate me")
        assertFalse(isUserActivated)
        Mockito.verify(userRepository, Mockito.times(0))
                .save(ArgumentMatchers.any(User::class.java))
    }

    /**
     * Представляет собой [ArgumentMatchers.any], сделано в качестве проверки
     */
    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    /**
     * Переопределение для [ArgumentMatchers.eq] <- char для возможности работы в Kotlin
     */
    private fun <T> any(value: T): T {
        reportMatcher(Equals(value))
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T

    /**
     * Переопределение [ArgumentMatchers.reportMatcher]: нужен для any(T)
     */
    private fun reportMatcher(matcher: ArgumentMatcher<*>) {
        ThreadSafeMockingProgress.mockingProgress().argumentMatcherStorage.reportMatcher(matcher)
    }
}