package com.example.techshopserver.users

import com.example.techshopserver.exception.BadRequestException
import com.example.techshopserver.security.Jwt
import com.example.techshopserver.users.Role.Companion.ADMIN
import com.example.techshopserver.users.Role.Companion.OPERATOR
import com.example.techshopserver.users.requests.LoginRequest
import com.example.techshopserver.users.requests.UserRequest
import com.example.techshopserver.users.responses.LoginResponse
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UsersService(val usersRepository: UsersRepository, val rolesRepository: RolesRepository, val jwt: Jwt) {

    fun save(req: UserRequest): User {
        val user = User(email = req.email!!, password = req.password!!, name = req.name!!)
        val userRole =
            rolesRepository.findByName(OPERATOR) ?: throw IllegalStateException("Role '$OPERATOR' not found.")
        user.roles.add(userRole)
        return usersRepository.save(user)
    }

    fun getById(id: Long) = usersRepository.findById(id)

    fun findAll(role: String?) =
        if (role == null) usersRepository.findAll().sortedBy { it.name } else usersRepository.findAllByRole(role)

    fun login(credentials: LoginRequest): LoginResponse? {
        var user = usersRepository.findByEmail(credentials.email!!) ?: return null
        if (user.password != credentials.password) return null
        log.info("User logged in. id={} name={}", user.id, user.name)
        return LoginResponse(token = jwt.createToken(user), user = user.toResponse())
    }

    fun delete(id: Long): Boolean {
        val user = usersRepository.findByIdOrNull(id) ?: return false
        if (user.roles.any { it.name == ADMIN }) {
            val adminCount = usersRepository.findAllByRole(ADMIN).count()
            if (adminCount == 1) throw BadRequestException("Cannot delete the last system admin.")
        }
        log.warn("User deleted. id={} name={}", user.id, user.name)
        usersRepository.delete(user)
        return true
    }

    companion object {
        val log = LoggerFactory.getLogger(UsersService::class.java)
    }
}