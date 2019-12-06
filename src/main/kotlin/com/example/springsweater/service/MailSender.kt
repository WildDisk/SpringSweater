package com.example.springsweater.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

/**
 * Сервис отправки сообщения на почтовый адрес
 * пользователя для подтверждения аккаунта
 *
 * @project SpringSweater
 * @author WildDisk on 06.12.2019
 */
@Service
class MailSender {
    @Autowired
    private lateinit var mailSender: JavaMailSender

    @Value("\${spring.mail.username}")
    private lateinit var username: String

    fun send(emailTo: String, subject: String, message: String) {
        val mailMessage = SimpleMailMessage()

        mailMessage.setFrom(username)
        mailMessage.setTo(emailTo)
        mailMessage.setSubject(subject)
        mailMessage.setText(message)

        mailSender.send(mailMessage)
    }
}