package com.example.springsweater.repository

import com.example.springsweater.domain.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * Интерфейс реализующий работу с API по сообщениям в NativeQuery
 *
 * @project SpringSweater
 * @author WildDisk
 */
interface MessageApiRepository : JpaRepository<Message, Long> {
    /**
     * Выборка всех сообщений пользователя.
     * Выводит id сообщения, текст, тэг и имя автора сообщения
     * @param username имя автора сообщений
     */
    @Query(value = """
        select 
        m.id as messageId,
        m.text as message,
        m.tag as tag,
        u.username as username
        from message m
        left join usr u on m.user_id = u.id
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