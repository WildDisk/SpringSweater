package com.example.springsweater.controller

import com.example.springsweater.domain.Message
import com.example.springsweater.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class MainController {
    @Autowired
    private lateinit var messageRepository: MessageRepository

    @GetMapping("/")
    fun greeting(): String = "greeting"

    @GetMapping("/main")
    fun main(model: Model): String {
        val messages: Iterable<Message> = messageRepository.findAll()
        model.addAttribute("messages", messages)
        return "main"
    }

    @PostMapping("/main")
    fun add(
            @RequestParam text: String,
            @RequestParam tag: String,
            model: Model
    ): String {
        val message = Message(text, tag)
        messageRepository.save(message)
        val messages: Iterable<Message> = messageRepository.findAll()
        model.addAttribute("messages", messages)
        return "main"
    }

    @RequestMapping("/apiMessageAl")
    fun apiMessageAll(): Iterable<Message> = messageRepository.findAll()

    @RequestMapping("/apiMessage")
    @ResponseBody
    fun apiMessage(@RequestParam messageId: Long): Message {
        val messages = messageRepository.findMessageById(messageId)
        return Message(messages[0].id, messages[0].text, messages[0].tag)
    }

    @PostMapping("/filter")
    fun filter(
            @RequestParam filter: String,
            model: Model
    ): String {
        val messages: Iterable<Message> = when {
            filter.isEmpty() -> messageRepository.findAll()
            else -> messageRepository.findByTag(filter)
        }
        model.addAttribute("messages", messages)
        return "main"
    }

    @GetMapping("/login")
    fun login(): String = "login"
}