package com.example.springsweater.controller

import com.example.springsweater.domain.Role
import com.example.springsweater.domain.User
import com.example.springsweater.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import java.util.*

/**
 * Класс конфугурации {@link WebSecurity}. Отвечает за ограничение доступа к страницам
 * авторизацию и шифрование
 *
 * @project SpringSweater
 * @author WildDisk
 */
@Controller
class RegistrationController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @GetMapping("/registration")
    fun registration(): String = "registration"

    /**
     * Регистрация нового пользователя
     * @param user нужен для поиска имеющихся пользователей с таким именем,
     * либо даёт зарегистрировать нового
     * @param model если пользователь с таким именем существует, возвращает
     * сообщение User exists!
     * @return registration если пользователь существует
     * @return redirect:/login пользователь зарегистрирован
     */
    @PostMapping("/registration")
    fun addUser(user: User, model: Model): String {
        val userCheck = userRepository.findByUsername(user.username)
        return when {
            userCheck != null -> {
                model.addAttribute("message", "User exists!")
                "registration"
            }
            else -> {
                user.isActive = true
                user.roles = Collections.singleton(Role.USER)
                userRepository.save(user)
                "redirect:/login"
            }
        }
    }
}