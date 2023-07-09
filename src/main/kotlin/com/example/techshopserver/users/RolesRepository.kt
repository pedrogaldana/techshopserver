package com.example.techshopserver.users

import com.example.techshopserver.users.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RolesRepository : JpaRepository<Role, Long> {
    fun findByName(name: String): Role?
}