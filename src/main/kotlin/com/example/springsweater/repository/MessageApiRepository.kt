package com.example.springsweater.repository

import com.example.springsweater.domain.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MessageApiRepository : JpaRepository<Message, Long> {
    @Query(value = """
        select 
        m.id as messageId,
        m.text as message,
        m.tag as tag,
        u.username as username
        from message m
        left join usr u on m.usr_id = u.id
        where u.username = ?1
    """, nativeQuery = true)
    fun findUserMessages(username: String): List<UsersMessage>

    companion object {
        interface UsersMessage {
            fun getMessageId(): String
            fun getMessage(): String
            fun getTag(): String
            fun getUsername(): String?
        }
    }
}