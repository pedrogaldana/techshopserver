package com.example.techshopserver.users

import com.example.techshopserver.users.Role.Companion.ADMIN
import com.example.techshopserver.users.Role.Companion.OPERATOR
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class UsersBootstrap(
    val rolesRepository: RolesRepository,
    val usersRepository: UsersRepository
) : ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val adminRole = Role(name = ADMIN)

        if (rolesRepository.count() == 0L) {
            rolesRepository.save(adminRole)
            rolesRepository.save(Role(name = OPERATOR))
        }

        if (usersRepository.count() == 0L) {
            val admin = User(
                email = "admin@techshopserver.com",
                password = "admin",
                name = "TechShop Server Administrator",
            )
            admin.roles.add(adminRole)
            usersRepository.save(admin)
        }
    }
}