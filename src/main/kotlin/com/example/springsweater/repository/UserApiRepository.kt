package com.example.springsweater.repository

import com.example.springsweater.domain.User
import org.springframework.data.repository.CrudRepository

interface UserApiRepository : CrudRepository<User, Long> {
    fun findUserById(id: Long): List<User>
    fun findUserByUsername(name: String): List<User>
}