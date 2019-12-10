package com.example.springsweater

import com.example.springsweater.controller.MainController
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
 *
 *
 * @project SpringSweater
 * @author WildDisk on 09.12.2019
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@WithUserDetails(value = "John")
class MainControllerTest {
    @Autowired
    private lateinit var controller: MainController
    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var createDataForTests: CreateDataForTest

    @BeforeAll
    fun setup() {
        createDataForTests.createUsers()
        createDataForTests.createMessages()
    }

    @Test
    fun mainPageTest() {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='navbarSupportedContent']/div").string("John"))
    }

    @Test
    fun messageListTest() {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(5))
    }

    @Test
    fun filterMessageTest() {
        this.mockMvc.perform(get("/main").param("filter", "first tag"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='message-list']/div").nodeCount(1))
                .andExpect(xpath("//*[@id='message-list']/div[@data-id='1']").exists())
    }

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

    @AfterAll
    fun teardown() {
        createDataForTests.deleteMessages()
        createDataForTests.deleteUsers()
    }
}