package com.example.springsweater.domain

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    USER;

    override fun getAuthority(): String = name
}