package com.example.springsweater.domain

/**
 * Обёртка над [Message] в [com.example.springsweater.api.ApiController] для передачи
 * данных в JSON через [com.example.springsweater.repository.MessageApiRepository] NativeQuery
 *
 * @project SpringSweater
 * @author WIldDisk
 */
class QMessage(
        val id: String,
        val text: String,
        val tag: String,
        val user: String
)