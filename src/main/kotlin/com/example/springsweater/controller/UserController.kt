package com.example.springsweater.controller

import com.example.springsweater.domain.Role
import com.example.springsweater.domain.User
import com.example.springsweater.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

/**
 * Редактирование профиля пользователя
 * Доступ к форме редактирования имеют пользователи
 * с профилем ADMIN
 *
 * @project SpringSweater
 * @author WildDisk
 */
@Controller
@RequestMapping("/user")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    /**
     * Вывод списка пользователей и всей информации по ним
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    fun userList(model: Model): String {
        model.addAttribute("users", userService.findAll())
        return "userList"
    }

    /**
     * Редактирование профиля пользователя
     * @param model user смена имени пользователя
     * @param model roles смена роли пользователя
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    fun userEdit(
            @PathVariable user: User,
            model: Model
    ): String {
        model.addAttribute("user", user)
        model.addAttribute("roles", Role.values())
        return "userEdit"
    }

    /**
     * Сохранение изменений профиля
     * Предворетельно очищается список ролей
     *
     * @param username
     * @param form
     * @param user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    fun userSave(
            @RequestParam username: String,
            @RequestParam form: Map<String, String>,
            @RequestParam("userId") user: User
    ): String? {
        userService.saveUser(user, username, form)
        return "redirect:/user"
    }

    /**
     * Получаем профиль авторизованного пользователя
     *
     * @param model
     * @param user
     */
    @GetMapping("profile")
    fun getProfile(model: Model, @AuthenticationPrincipal user: User): String {
        model.addAttribute("username", user.username)
        model.addAttribute("email", user.email)
        return "profile"
    }

    @PostMapping("profile")
    fun updateProfile(
            @AuthenticationPrincipal user: User,
            @RequestParam password: String,
            @RequestParam email: String
    ): String {
        userService.updateProfile(user, password, email)
        return "redirect:/user/profile"
    }
}