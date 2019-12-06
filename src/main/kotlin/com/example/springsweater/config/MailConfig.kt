package com.example.springsweater.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

/**
 * Конфиг настройки сервиса email активации
 *
 * <host> smtp.gmail.com smtp хост
 * <username> wilddisk.spring@gmail.com почта для отправки сообщений
 * <port> 465 порт для smtp
 * <protocol> smtps
 *
 * @project SpringSweater
 * @author WildDisk on 06.12.2019
 */
@Configuration
class MailConfig {
    @Value("\${spring.mail.host}")
    private lateinit var host: String

    @Value("\${spring.mail.username}")
    private lateinit var username: String

    @Value("\${spring.mail.password}")
    private lateinit var password: String

    @Value("\${spring.mail.port}")
    private var port: Int = JavaMailSenderImpl.DEFAULT_PORT

    @Value("\${spring.mail.protocol}")
    private lateinit var protocol: String

    @Value("\${mail.debug}")
    private lateinit var debug: String

    /**
     * Bean для получения коннекта к mail сервису
     */
    @Bean
    fun getMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port
        mailSender.username = username
        mailSender.password = password
        val properties: Properties = mailSender.javaMailProperties
        properties.setProperty("mail.transport.protocol", protocol)
        properties.setProperty("mail.debug", debug)
        return mailSender
    }
}