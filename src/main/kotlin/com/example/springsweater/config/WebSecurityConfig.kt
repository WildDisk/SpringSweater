package com.example.springsweater.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import javax.sql.DataSource

@Configuration
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var dataSource: DataSource

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

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.jdbcAuthentication()
                ?.dataSource(dataSource)
                ?.passwordEncoder(NoOpPasswordEncoder.getInstance())
                ?.usersByUsernameQuery("select user_name, password, is_active from usr where user_name=?")
                ?.authoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on u.id = ur.user_id where u.username=?")
    }
}