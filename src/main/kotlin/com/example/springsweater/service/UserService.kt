package com.example.springsweater.service

import org.springframework.stereotype.Service

import com.example.springsweater.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * Сервис отвечающий за аутентификацию пользователей
 *
 * @project SpringSweater
 * @author WildDisk
 */
@Service
class UserService : UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    /**
     * Отвечает за поиск имеющихся пользователей, если есть,
     * отдаёт на аутентификацию
     * @throws UsernameNotFoundException
     * @param username
     * @return UserDetails
     */
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails? = username?.let { userRepository.findByUsername(it) }
}