package com.example.springsweater.controller

import com.example.springsweater.controller.ControllerUtils.getErrors
import com.example.springsweater.domain.User
import com.example.springsweater.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.validation.Valid

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
     * @param passwordConfirm подтверждение ввода пароля из password2
     * @return registration если пользователь существует
     * @return redirect:/login пользователь зарегистрирован
     */
    @PostMapping("/registration")
    fun addUser(
            @RequestParam("password2") passwordConfirm: String,
            @Valid user: User,
            bindingResult: BindingResult,
            model: Model
    ): String {
        val isConfirmEmpty = StringUtils.isEmpty(passwordConfirm)
        when {
            isConfirmEmpty -> model.addAttribute("password2Error", "Password confirmation cannot be empty")
        }
        return when {
            !user.password.equals(passwordConfirm) -> {
                model.addAttribute("passwordError", "Passwords are different!")
                "registration"
            }
            isConfirmEmpty || bindingResult.hasErrors() -> {
                val errors = getErrors(bindingResult)
                model.mergeAttributes(errors)
                "registration"
            }
            else -> when {
                !userService.addUser(user) -> {
                    model.addAttribute("usernameError", "User exists")
                    "registration"
                }
                else -> "redirect:/login"
            }
        }
    }

    /**
     * Активация пользователя
     *
     * @param model сообщение о результате активации
     * @param code код активации из письма
     */
    @GetMapping("/activate/{code}")
    fun activate(model: Model, @PathVariable code: String): String {
        val isActivated: Boolean = userService.activateUser(code)
        when {
            isActivated -> model.addAttribute("message", "User successfully activated")
            else -> model.addAttribute("message", "Activation code is not found")
        }
        return "login"
    }
}
