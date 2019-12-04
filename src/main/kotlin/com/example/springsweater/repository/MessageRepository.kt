package com.example.springsweater.repository

import com.example.springsweater.domain.Message
import org.springframework.data.repository.CrudRepository

interface MessageRepository : CrudRepository<Message, Long> {
    fun findMessageById(id: Long): List<Message>
    fun findByTag(tag: String): List<Message>
}