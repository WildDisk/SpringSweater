package com.example.springsweater

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringSweaterApplication

fun main(args: Array<String>) {
	runApplication<SpringSweaterApplication>(*args)
}
