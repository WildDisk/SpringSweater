package com.example.springsweater.repository

import com.example.springsweater.domain.Message
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

/**
 * Интерфейс для работы с сообщениями
 *
 * @project SpringSweater
 * @author WildDisk
 */
interface MessageRepository : CrudRepository<Message, Long> {
    fun findMessageById(id: Long): List<Message>
    fun findByTag(tag: String, pageable: Pageable): Page<Message>
    fun findAll(pageable: Pageable): Page<Message>
}