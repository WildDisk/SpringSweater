package com.example.springsweater.controller

import com.example.springsweater.domain.Message
import com.example.springsweater.domain.User
import com.example.springsweater.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
class MessageController {
    @Autowired
    private lateinit var messageRepository: MessageRepository
    @Value("\${upload.path}")
    private lateinit var uploadPath: String

    /**
     * Приветствие
     */
    @GetMapping("/")
    fun greeting(): String = "greeting"

    /**
     * Вывод постов на экран
     *
     * @param filter отвечает за фильтрацию постов по тэгу
     */
    @GetMapping("/main")
    fun main(
            @AuthenticationPrincipal user: User,
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
            @RequestParam(required = false, defaultValue = "") filter: String,
            @RequestParam("file") file: MultipartFile?
    ): String {
        message.author = user
        when {
            bindingResult.hasErrors() -> {
                val errorsMap = ControllerUtils.getErrors(bindingResult)
                model.mergeAttributes(errorsMap)
                model.addAttribute("message", message)
            }
            else -> {
                saveFile(file, message)
                model.addAttribute("message", null)
                messageRepository.save(message)
            }
        }
        val messages: Iterable<Message> = messageRepository.findAll()
        model.addAttribute("messages", messages)
        return "main"
    }

    /**
     * Присвоение [UUID] и сохранение файла
     *
     * @param file сохраняемый файл
     * @param message
     */
    private fun saveFile(file: MultipartFile?, message: Message) {
        when {
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

    /**
     * Получение списка сообщений авторизованного пользователя
     *
     * @param currentUser авторизованный пользователь
     * @param user .messages сообщения пользователя
     * @param model
     * @param message определяет message.id в messageEdit.ftl
     */
    @GetMapping("/user-messages/{user}")
    fun userMessages(
            @AuthenticationPrincipal currentUser: User,
            @PathVariable user: User,
            model: Model,
            @RequestParam(required = false) message: Message?,
            @PageableDefault(sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable
    ): String {
        val messages = user.messages
        model.addAttribute("userChannel", user)
        model.addAttribute("subscriptionsCount", user.subscriptions.size)
        model.addAttribute("subscribersCount", user.subscribers.size)
        model.addAttribute("isSubscriber", user.subscribers.contains(currentUser))
        model.addAttribute("messages", messages)
        model.addAttribute("isCurrentUser", currentUser == user)
        model.addAttribute("message", message)
        return "userMessages"
    }

    /**
     * Редактирование сообщения пользователя
     *
     * @param currentUser пользователь
     * @param user id пользователя, отдаётся в redirect
     */
    @Throws(IOException::class)
    @PostMapping("/user-messages/{user}")
    fun updateMessage(
            @AuthenticationPrincipal currentUser: User,
            @PathVariable user: Long,
            @RequestParam("id") message: Message,
            @RequestParam("text") text: String,
            @RequestParam("tag") tag: String,
            @RequestParam("file") file: MultipartFile?
    ): String {
        if (message.author == currentUser) {
            if (!StringUtils.isEmpty(text)) message.text = text
            if (!StringUtils.isEmpty(tag)) message.tag = tag
            saveFile(file, message)
            messageRepository.save(message)
        }
        return "redirect:/user-messages/$user"
    }
}