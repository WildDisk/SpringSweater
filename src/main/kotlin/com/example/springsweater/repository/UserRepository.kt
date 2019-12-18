package com.example.springsweater.repository

import com.example.springsweater.domain.User
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Интерфейс для работы с пользователями
 *
 * @project SpringSweater
 * @author WildDisk
 */
interface UserRepository : JpaRepository<User, Long> {
    /**
     * Поиск пользователя по имени
     *
     * @param username искомого пользователя
     * @return User? Если без ? падает при передачи параметров из шаблона
     */
    fun findByUsername(username: String): User?

    /**
     * Активация пользователя по коду
     *
     * @param code активации
     */
    fun findByActivationCode(code: String): User
}