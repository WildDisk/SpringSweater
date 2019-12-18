package com.example.springsweater.config

import com.example.springsweater.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Конфигурационный класс для доступа к страницам, аутентификации
 * и шифрования паролей
 *
 * @project SpringSweater
 * @author WildDisk
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    /**
     * Bean [PasswordEncoder] для шифрования паролей с силой 8
     */
    @Bean
    fun getPasswordEncoder(): PasswordEncoder = BCryptPasswordEncoder(8)

    /**
     * Конфиг доступа к страницам и сохранение сессии
     *
     * @param http
     */
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                    .antMatchers("/", "/greeting", "/registration", "/api/**", "/static/**", "/activate/*").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login").permitAll()
                .and()
                    .rememberMe()
                .and()
                    .logout().permitAll()
    }

    /**
     * Конфиг аутентификации userService передаёт пользователя в
     * [org.springframework.security.core.userdetails.UserDetailsService]
     * для его аутентификации
     *
     * @param auth
     */
    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userService)
                ?.passwordEncoder(passwordEncoder)
    }
}