package com.example.springsweater.controller

import com.example.springsweater.domain.User
import com.example.springsweater.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

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
    private lateinit var userService: UserService

    @GetMapping("/registration")
    fun registration(): String = "registration"

    /**
     * Регистрация нового пользователя
     *
     * @param user нужен для поиска имеющихся пользователей с таким именем,
     * либо даёт зарегистрировать нового
     * @param model если пользователь с таким именем существует, возвращает
     * сообщение User exists!
     * @return registration если пользователь существует
     * @return redirect:/login пользователь зарегистрирован
     */
    @PostMapping("/registration")
    fun addUser(user: User, model: Model): String = when {
        !userService.addUser(user) -> {
            model.addAttribute("message", "User exists!")
            "registration"
        }
        else -> "redirect:/login"
    }

    /**
     * Активация пользователя
     *
     * @param model сообщение о результате активации
     * @param code код активации из письма
     */
    fun activate(model: Model, @PathVariable code: String): String {
        val isActivated: Boolean = userService.activateUser(code)
        when {
            isActivated -> model.addAttribute("message", "User successfully activated")
            else -> model.addAttribute("message", "Activation code is not found")
        }
        return "login"
    }
}
