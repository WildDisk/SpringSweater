package com.example.springsweater.domain

import org.hibernate.validator.constraints.Length
import javax.persistence.*
import javax.validation.constraints.NotBlank

/**
 * Message класс отвечает за хранение данных
 * в таблице message
 *
 * @param id поста
 * @param text текст поста
 * @param tag поста
 * @param author имя автора поста
 * @param filename имя файла поста, изображение
 *
 * @project SpringSweater
 * @author WildDisk
 */
@Entity
class Message(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,
        @get: NotBlank(message = "Please fill the message")
        @get: Length(max = 2048, message = "Message to long more than(2kb)")
        var text: String? = null,
        @get: Length(max = 255, message = "Tag to long(no more than 255 characters)")
        var tag: String? = null,
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id")
        var author: User? = null,
        var filename: String? = null
) {
    constructor(text: String, tag: String) : this(id = 0, text = text, tag = tag)
    constructor(text: String, tag: String, user: User) : this(id = 0, text = text, tag = tag, author = user)

    /**
     * Получает имя автора поста, если null то возвращает {none}
     */
    fun getAuthorName(): String = author?.username ?: "{none}"
}