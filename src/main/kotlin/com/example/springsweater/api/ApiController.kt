package com.example.springsweater.api

import com.example.springsweater.domain.Message
import com.example.springsweater.domain.User
import com.example.springsweater.repository.MessageRepository
import com.example.springsweater.repository.UserApiRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.lang.IndexOutOfBoundsException

@RestController
class ApiController {
    @Autowired
    private lateinit var messageRepository: MessageRepository
    @Autowired
    private lateinit var userApiRepository: UserApiRepository

    @RequestMapping("/api/messages")
    @ResponseBody
    fun apiMessageAll(): Iterable<Message> = messageRepository.findAll()

    @RequestMapping("/api/message")
    @ResponseBody
    fun apiMessage(@RequestParam id: Long): Message {
        val messages = messageRepository.findMessageById(id)
        return Message(messages[0].id, messages[0].text, messages[0].tag)
    }

    @RequestMapping("/api/users")
    @ResponseBody
    fun apiUserAll(): Iterable<User> = userApiRepository.findAll()

    @RequestMapping("/api/user")
    @ResponseBody
    fun apiUserById(@RequestParam id: Long): User = try {
        val users = userApiRepository.findUserById(id)
        User(users[0].id, users[0].username, users[0].password, users[0].isActive, users[0].roles)
    } catch (e: IndexOutOfBoundsException) {
        User(0, "Пользователь не найден", "null", false, mutableSetOf())
    }

    @RequestMapping("/api/username")
    @ResponseBody
    fun apiUserByName(@RequestParam name: String): User = try {
        val users = userApiRepository.findUserByUsername(name)
        User(users[0].id, users[0].username, users[0].password, users[0].isActive, users[0].roles)
    } catch (e: IndexOutOfBoundsException) {
        User(0, "Пользователь не найден", "null", false, mutableSetOf())
    }
}