package com.example.springsweater.config

import com.example.springsweater.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder

/**
 * Конфигурационный класс для доступа к страницам и аутентификации
 * {@link UserService} отвечает за поиск пользователя и его аутентификацию
 *
 * @project SpringSweater
 * @author WildDisk
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var userService: UserService

    /**
     * Конфиг доступа к страницам
     * {@link .antMatchers()} страницы к оторым есть доступ у всех
     * {@link permitAll()} разрешает доступ всем
     * @param http
     */
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                    .antMatchers("/", "/greeting", "/registration", "/api/*").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login").permitAll()
                .and()
                    .logout().permitAll()
    }

    /**
     * Конфиг аутентификации
     * userService передаёт пользователя в {@link .userDetailService}
     * для его аутентификации
     * @param auth
     */
    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userService)
                ?.passwordEncoder(NoOpPasswordEncoder.getInstance())
    }
}