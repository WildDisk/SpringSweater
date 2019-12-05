package com.example.springsweater.controller

import com.example.springsweater.domain.Role
import com.example.springsweater.domain.User
import com.example.springsweater.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collectors

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
@PreAuthorize("hasAuthority('ADMIN')")
class UserController {
    @Autowired
    private lateinit var userRepository: UserRepository

    /**
     * Вывод списка пользователей и всей информации по ним
     */
    @GetMapping
    fun userList(model: Model): String {
        model.addAttribute("users", userRepository.findAll())
        return "userList"
    }

    /**
     * Редактирование профиля пользователя
     * @param model user смена имени пользователя
     * @param model roles смена роли пользователя
     */
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
     */
    @PostMapping
    fun userSave(
            @RequestParam username: String,
            @RequestParam form: Map<String, String>,
            @RequestParam("userId") user: User
    ): String? {
        user.username = username
        val roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet())
        user.roles.clear()
        form.forEach {
            if (roles.contains(it.key)) user.roles.add(Role.valueOf(it.key))
        }
        userRepository.save(user)
        return "redirect:/user"
    }
}