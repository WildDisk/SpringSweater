package com.example.springsweater.domain

import org.springframework.security.core.GrantedAuthority

/**
 * Роли пользователей
 *
 * @project SpringSweater
 * @author WIldDisk
 */
enum class Role : GrantedAuthority {
    USER, ADMIN;

    override fun getAuthority(): String = name
}