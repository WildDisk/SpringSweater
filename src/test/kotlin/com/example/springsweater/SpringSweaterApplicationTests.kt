package com.example.springsweater

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SpringSweaterApplicationTests {

	@BeforeAll
	fun setup() {
		println(">> Setup")
	}

	@Test
	fun contextLoads() {
		println(">> Assert block")
	}

	@AfterAll
	fun teardown() {
		println(">> Tear down")
	}
}
