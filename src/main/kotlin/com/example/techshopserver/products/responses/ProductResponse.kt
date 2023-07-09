package com.example.techshopserver.products.responses

import java.math.BigDecimal

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val category: String
)