package com.example.springsweater

import com.example.springsweater.controller.MessageController
import com.example.springsweater.createDataTests.CreateDataForTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath

/**
 * Интеграционные тесты на создание новых постов
 *
 * @project SpringSweater
 * @author WildDisk on 09.12.2019
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@WithUserDetails(value = "John")
class MessageControllerTest {
    @Autowired
    private lateinit var controller: MessageController
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var createDataForTests: CreateDataForTest

    /**
     * Пересоздание пользователей и сообщений в тестовой базе данных перед использованием тестов
     */
    @BeforeAll
    fun setup() {
        createDataForTests.createUsers()
        createDataForTests.createMessages()
    }

    /**
     * Отображение имени пользователя на странице при аутентификации
     */
    @Test
    fun mainPageTest() {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='navbarSupportedContent']/div").string("John"))
    }

    /**
     * Количество сообщений на странице
     */
    @Test
    fun messageListTest() {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(5))
    }

    /**
     * Тестирование фильтрации сообщений по тегу
     */
    @Test
    fun filterMessageTest() {
        this.mockMvc.perform(get("/main").param("filter", "first tag"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(1))
                .andExpect(xpath("//*[@id='message-list']/div[@data-id='1']").exists())
    }

    /**
     * Тестирование создания нового поста на сайте
     */
    @Test
    fun addMessageToListTest() {
        val multipart = multipart("/main")
                .file("file", "123".toByteArray())
                .param("text", "fifth")
                .param("tag", "new one")
                .with(csrf())
        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(5))
                .andExpect(xpath("//*[@id='message-list']/div[@data-id='10']").exists())
                .andExpect(xpath("//*[@id='message-list']/div[@data-id='10']/div/span").string("fifth"))
                .andExpect(xpath("//*[@id='message-list']/div[@data-id='10']/div/i").string("#new one"));
    }

    /**
     * Удаление сообщений и пользователей из тестовой базы данных после использования тестов
     */
    @AfterAll
    fun teardown() {
        createDataForTests.deleteMessages()
        createDataForTests.deleteUsers()
    }
}