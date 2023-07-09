package com.example.techshopserver.users

import com.example.techshopserver.users.Role.Companion.ADMIN
import com.example.techshopserver.users.requests.LoginRequest
import com.example.techshopserver.users.requests.UserRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
@SecurityScheme(name = "techshopserver", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
class UsersController(val service: UsersService) {

    @Operation(summary = "Lista todos os usuários")
    @GetMapping
    fun listUsers(@RequestParam("role") role: String?) = service.findAll(role).map { it.toResponse() }

    @Operation(summary = "Cadastra um usuário")
    @Transactional
    @SecurityRequirement(name = "techshopserver")
    @PreAuthorize("hasRole('$ADMIN')")
    @PostMapping
    fun createUser(@RequestBody @Valid req: UserRequest) =
        service.save(req)
            .toResponse()
            .let { ResponseEntity.status(CREATED).body(it) }


    @Operation(summary = "Retorna o usuário logado")
    @GetMapping("/me")
    @PreAuthorize("permitAll()")
    @SecurityRequirement(name = "techshopserver")
    fun getSelf(auth: Authentication) = getUser(auth.credentials as Long)

    @Operation(summary = "Retorna o usuário através do seu id")
    @GetMapping("/{id}")
    fun getUser(@PathVariable("id") id: Long) =
        service.getById(id).orElse(null)
            ?.let { ResponseEntity.ok(it.toResponse()) }
            ?: ResponseEntity.notFound().build()


    @Operation(summary = "Efetua o login no sistema")
    @PostMapping("/login")
    fun login(@Valid @RequestBody credentials: LoginRequest) =
        service.login(credentials)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()


    @Operation(summary = "Deleta um usuário através do seu id")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('$ADMIN')")
    @SecurityRequirement(name = "techshopserver")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Void> =
        if (service.delete(id)) ResponseEntity.ok().build() else ResponseEntity.notFound().build()


}