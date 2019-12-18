package com.example.springsweater

import com.example.springsweater.createDataTests.CreateDataForTest
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


/**
 * Интеграционные тесты на авторизацию
 *
 * @project SpringSweater
 * @author WildDisk on 09.12.2019
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class LoginTest(@Autowired val mockMvc: MockMvc) {
    @Autowired
    private lateinit var createDataForTest: CreateDataForTest

    /**
     * Пересоздание пользователей в тестовой базе данных перед использованием тестов
     */
    @BeforeAll
    fun setup() {
        createDataForTest.createUsers()
    }

    /**
     * Проверяем наличие текста на фронте
     */
    @Test
    fun contextLoads() {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(content().string(containsString("Hello, guest!")))
                .andExpect(content().string(containsString("Please, login!")))
    }

    /**
     * Проверяем редирект при отстутствии авторизации на сайте, на страницу login
     */
    @Test
    fun accessDeniedTest() {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("http://localhost/login"))
    }

    /**
     * Подтверждение аутентификации
     */
    @Test
    fun correctLoginTest() {
        this.mockMvc.perform(formLogin().user("admin").password("admin"))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("/"))
    }

    /**
     * Ошибка аутентификации при отсутствии пользователя
     */
    @Test
    fun badCredentials() {
        this.mockMvc.perform(post("/login").param("username", "John"))
                .andDo(print())
                .andExpect(status().isForbidden)
    }

    /**
     * Удаление пользователей из тестовой базы данных после использования тестов
     */
    @AfterAll
    fun teardown() {
        createDataForTest.deleteUsers()
    }
}