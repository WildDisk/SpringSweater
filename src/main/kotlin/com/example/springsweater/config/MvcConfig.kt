package com.example.springsweater.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Конфигуратор для настройки опций сопоставления путей "/"
 * а так же работа со статическими ресурсами
 *
 * @project SpringSweater
 * @author WildDisk
 */
@Configuration
class MvcConfig : WebMvcConfigurer {
    @Value("\${upload.path}")
    private lateinit var uploadPath: String

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/login").setViewName("login")
    }

    /**
     * Handler для сохранения статических ресурсов "img"
     *
     * @param registry
     */
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file://$uploadPath/")
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
    }
}