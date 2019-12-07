package com.example.springsweater.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

/**
 * User класс отвечает за хранение данных
 * в таблице usr
 *
 * @param id id пользователя
 * @param username имя пользователя
 * @param password хэш пароля
 * @param isActive статус активации пользователя
 * @param email пользователя
 * @param activationCode код активации для регистрации по email
 * @param roles роли для пользователей
 * @param messages созданные сообщения пользователя
 *
 * @project SpringSweater
 * @author WildDisk
 */
@Entity
@Table(name = "usr")
class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,
        @field: NotBlank(message = "Username cannot be empty")
        private var username: String = "",
        @field: NotBlank(message = "Password cannot be empty")
        private var password: String = "",
        var isActive: Boolean = false,
        @get: Email(message = "Email is not correct")
        @get: NotBlank(message = "Email cannot be empty")
        var email: String = "",
        var activationCode: String? = "",
        @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
        @CollectionTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id")])
        @Enumerated(EnumType.STRING)
        var roles: MutableSet<Role> = mutableSetOf(Role.USER),
        @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        var messages: Set<Message> = setOf(Message())
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = roles

    override fun isEnabled(): Boolean = isActive

    override fun getUsername(): String? = username

    fun setUsername(username: String) {
        this.username = username
    }

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String? = password

    fun setPassword(password: String) {
        this.password = password
    }

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    fun isAdmin(): Boolean = roles.contains(Role.ADMIN)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}