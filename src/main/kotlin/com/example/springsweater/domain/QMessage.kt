package com.example.springsweater.domain

/**
 * Обёртка над {@link Message} в ApiController для передачи
 * данных в JSON через {@link MessageApiRepository} NativeQuery
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