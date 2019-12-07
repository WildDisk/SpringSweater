package com.example.springsweater.controller

import com.example.springsweater.controller.ControllerUtils.getErrors
import com.example.springsweater.domain.User
import com.example.springsweater.domain.dto.CaptchaResponseDto
import com.example.springsweater.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.RestTemplate
import java.util.*
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
    @Value("\${recaptcha.secret}")
    private lateinit var secret: String
    @Autowired
    private lateinit var restTemplate: RestTemplate

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
            @RequestParam("g-recaptcha-response") captchaResponse: String,
            @Valid user: User,
            bindingResult: BindingResult,
            model: Model
    ): String {
        val url = String.format(CAPTCHA_URL, secret, captchaResponse)
        val response: CaptchaResponseDto? = restTemplate.postForObject(url, Collections.EMPTY_LIST, CaptchaResponseDto::class.java)
        val successfullyResponse: Boolean
        when(response?.success) {
            false -> {
                successfullyResponse = false
                model.addAttribute("captchaError", "Fill captcha")
            }
            else -> successfullyResponse = true
        }
        val isConfirmEmpty = StringUtils.isEmpty(passwordConfirm)
        when {
            isConfirmEmpty -> model.addAttribute("password2Error", "Password confirmation cannot be empty")
        }
        return when {
            !user.password.equals(passwordConfirm) -> {
                model.addAttribute("passwordError", "Passwords are different!")
                "registration"
            }
            isConfirmEmpty || bindingResult.hasErrors() || !successfullyResponse -> {
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
            isActivated -> {
                model.addAttribute("messageType", "success")
                model.addAttribute("message", "User successfully activated")
            }
            else -> {
                model.addAttribute("messageType", "danger")
                model.addAttribute("message", "Activation code is not found!")
            }
        }
        return "login"
    }

    companion object {
        private const val CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s"
    }
}
