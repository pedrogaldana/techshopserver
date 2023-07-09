package com.example.techshopserver.products

import com.example.techshopserver.products.requests.ProductRequest
import com.example.techshopserver.users.Role.Companion.ADMIN
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
@SecurityScheme(name = "techshopserver", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
class ProductsController(val service: ProductsService) {

    @Operation(summary = "Retorna todos os produtos")
    @GetMapping
    fun listProducts(@RequestParam("category") category: String?) = service.findAll(category).map { it.toResponse() }

    @Operation(summary = "Cadastra um produto novo")
    @Transactional
    @PreAuthorize("hasRole('$ADMIN')")
    @SecurityRequirement(name = "techshopserver")
    @PostMapping
    fun createProduct(@RequestBody @Valid req: ProductRequest) =
        service.save(req)
            .toResponse()
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }


    @Operation(summary = "Retorna o produto através do seu id")
    @GetMapping("/{id}")
    fun getProduct(@PathVariable("id") id: Long) =
        service.getById(id).orElse(null)
            ?.let { ResponseEntity.ok(it.toResponse()) }
            ?: ResponseEntity.notFound().build()


    @Operation(summary = "Deleta o produto através do seu id")
    @PreAuthorize("hasRole('$ADMIN')")
    @SecurityRequirement(name = "techshopserver")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<String> =
        if (service.delete(id)) ResponseEntity.ok("Produto deletado com sucesso!")
        else ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado")


    @Operation(summary = "Atualiza o produto que possui o mesmo id")
    @PreAuthorize("hasRole('$ADMIN')")
    @SecurityRequirement(name = "techshopserver")
    @Transactional
    @PutMapping
    fun updateProduct(@RequestBody @Valid req: ProductRequest): ResponseEntity<String> =
        if (service.update(req)) ResponseEntity.ok("Produto atualizado com sucesso!")
        else ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado")
}