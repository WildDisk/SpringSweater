package com.example.springsweater

import com.example.springsweater.createDataTests.CreateDataForTest
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
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
 *
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

    @BeforeAll
    fun setup() {
        createDataForTest.createUsers()
    }

    @Test
    fun contextLoads() {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(content().string(containsString("Hello, guest!")))
                .andExpect(content().string(containsString("Please, login!")))
    }

    @Test
    fun accessDeniedTest() {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("http://localhost/login"))
    }

    @Test
    fun correctLoginTest() {
        this.mockMvc.perform(formLogin().user("admin").password("admin"))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("/"))
    }

    @Test
    fun badCredentials() {
        this.mockMvc.perform(post("/login").param("username", "John"))
                .andDo(print())
                .andExpect(status().isForbidden)
    }

    @AfterAll
    fun teardown() {
        createDataForTest.deleteUsers()
    }
}