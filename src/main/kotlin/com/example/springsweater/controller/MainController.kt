package com.example.springsweater.controller

import com.example.springsweater.domain.Message
import com.example.springsweater.domain.User
import com.example.springsweater.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class MainController {
    @Autowired
    private lateinit var messageRepository: MessageRepository

    @GetMapping("/")
    fun greeting(
            @AuthenticationPrincipal user: User?,
            model: Model
    ): String {
        when {
            user != null -> model.addAttribute("user", user.username)
            else -> model.addAttribute("user", "guest!")
        }
        return "greeting"
    }

    @GetMapping("/main")
    fun main(
            @RequestParam(required = false, defaultValue = "") filter: String,
            model: Model
    ): String {
        val messages: Iterable<Message> = when {
            filter.isEmpty() -> messageRepository.findAll()
            else -> messageRepository.findByTag(filter)
        }
        model.addAttribute("messages", messages)
        model.addAttribute("filter", filter)
        return "main"
    }

    @PostMapping("/main")
    fun add(
            @AuthenticationPrincipal user: User,
            @RequestParam text: String,
            @RequestParam tag: String,
            model: Model
    ): String {
        val message = Message(text, tag, user)
        messageRepository.save(message)
        val messages: Iterable<Message> = messageRepository.findAll()
        model.addAttribute("messages", messages)
        return "main"
    }

    @GetMapping("/login")
    fun login(): String = "login"
}