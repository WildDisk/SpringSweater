package com.example.springsweater.controller

import com.example.springsweater.domain.Message
import com.example.springsweater.domain.User
import com.example.springsweater.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.util.*
import javax.validation.Valid

/**
 * Контроллер отвечающий за посты и страницу приветствия
 *
 * @project SpringSweater
 * @author WildDisk
 */
@Controller
class MainController {
    @Autowired
    private lateinit var messageRepository: MessageRepository
    @Value("\${upload.path}")
    private lateinit var uploadPath: String

    /**
     * Приветствие
     *
     * @param user смотрим авторизован пользователь или нет
     * @param model если пользователь авторизован возвращается имя или "guest"
     */
    @GetMapping("/")
    fun greeting(
            @AuthenticationPrincipal user: User?,
            model: Model
    ): String {
        when {
            user != null -> model.addAttribute("user", user.username)
            else -> model.addAttribute("user", "guest")
        }
        return "greeting"
    }

    /**
     * Вывод постов на экран
     *
     * @param filter отвечает за фильтрацию постов по тэгу
     */
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

    /**
     * Создание поста
     *
     * @param user автор поста
     * @param file изображение в посте
     */
    @Throws(IOException::class)
    @PostMapping("/main")
    fun add(
            @AuthenticationPrincipal user: User,
            @Valid message: Message,
            bindingResult: BindingResult,
            model: Model,
            @RequestParam("file") file: MultipartFile?
    ): String {
        message.author = user
        when {
            bindingResult.hasErrors() -> {
                val errorsMap = ControllerUtils.getErrors(bindingResult)
                model.mergeAttributes(errorsMap)
                model.addAttribute("message", message)
            }
            else -> when {
                file != null && file.originalFilename.toString().isNotEmpty() -> {
                    val uploadDir = File(uploadPath)
                    when {
                        !uploadDir.exists() -> uploadDir.mkdir()
                    }
                    val uuid = UUID.randomUUID().toString()
                    val resultFileName = "$uuid.${file.originalFilename}"
                    file.transferTo(File("$uploadPath/$resultFileName"))
                    message.filename = resultFileName
                }
            }
        }
        messageRepository.save(message)
        val messages: Iterable<Message> = messageRepository.findAll()
        model.addAttribute("messages", messages)
        return "main"
    }

    @GetMapping("/login")
    fun login(): String = "login"
}