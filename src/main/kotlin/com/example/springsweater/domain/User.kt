package com.example.springsweater.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name = "usr")
class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,
        private var username: String = "",
        private var password: String = "",
        var isActive: Boolean = false,
        @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
        @CollectionTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id")])
        @Enumerated(EnumType.STRING)
        var roles: MutableSet<Role> = mutableSetOf(Role.USER)
) : UserDetails {
        override fun getAuthorities(): Collection<GrantedAuthority> = roles

        override fun isEnabled(): Boolean = isActive

        override fun getUsername(): String = username

        fun setUsername(username: String) {
                this.username = username
        }

        override fun isCredentialsNonExpired(): Boolean = true

        override fun getPassword(): String = password

        fun setPassword(password: String) {
                this.password = password
        }

        override fun isAccountNonExpired(): Boolean = true

        override fun isAccountNonLocked(): Boolean = true

        fun isAdmin(): Boolean = roles.contains(Role.USER)
}