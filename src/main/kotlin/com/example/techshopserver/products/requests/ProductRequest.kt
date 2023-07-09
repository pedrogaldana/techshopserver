package com.example.techshopserver.products.requests

import jakarta.annotation.Nullable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class ProductRequest(
    @field:Nullable
    val id: Long?,

    @field:NotBlank
    val name: String?,

    @field:NotBlank
    val description: String?,

    @field:NotNull
    val price: BigDecimal?,

    @field:NotBlank
    val category: String?,
)