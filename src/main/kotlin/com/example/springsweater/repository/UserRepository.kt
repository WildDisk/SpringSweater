package com.example.springsweater.repository

import com.example.springsweater.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    /**
     * "User?" Если без ? падает при передачи параметров из шаблона
     */
    fun findByUsername(username: String): User?
}