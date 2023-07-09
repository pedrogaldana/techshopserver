package com.example.techshopserver.orders

import com.example.techshopserver.orders.requests.OrderRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
@SecurityScheme(name = "techshopserver", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
class OrdersController(val service: OrdersService) {

    @Operation(summary = "Retorna todos os pedidos")
    @GetMapping
    fun listOrders(@RequestParam("status") status: String?) = service.findAll(status).map { it.toResponse() }

    @Operation(summary = "Cadastra um pedido novo através do id do primeiro produto")
    @Transactional
    @SecurityRequirement(name = "techshopserver")
    @PostMapping
    fun createOrder(@RequestBody @Valid req: OrderRequest) =
        service.save(req)
            .toResponse()
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }


    @Operation(summary = "Retorna o pedido através do seu id")
    @GetMapping("/{id}")
    fun getOrder(@PathVariable("id") id: Long) =
        service.getById(id).orElse(null)
            ?.let { ResponseEntity.ok(it.toResponse()) }
            ?: ResponseEntity.notFound().build()


    @Operation(summary = "Adiciona produto ao pedido")
    @Transactional
    @SecurityRequirement(name = "techshopserver")
    @PutMapping("/product/add")
    fun addProduct(@RequestBody @Valid req: OrderRequest): ResponseEntity<String> =
        if (service.addProduct(req)) ResponseEntity.ok("Pedido atualizado com sucesso!")
        else ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido ou produto não encontrado")


    @Operation(summary = "Remove produto do pedido")
    @Transactional
    @SecurityRequirement(name = "techshopserver")
    @PutMapping("/product/remove")
    fun removeProduct(@RequestBody @Valid req: OrderRequest): ResponseEntity<String> =
        if (service.removeProduct(req)) ResponseEntity.ok("Pedido atualizado com sucesso!")
        else ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido ou produto não encontrado")


    @Operation(summary = "Finaliza o pedido")
    @Transactional
    @SecurityRequirement(name = "techshopserver")
    @PutMapping("/conclude")
    fun concludeOrder(@RequestBody @Valid req: OrderRequest): ResponseEntity<String> =
        if (service.conclude(req)) ResponseEntity.ok("Pedido atualizado com sucesso!")
        else ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado")
}