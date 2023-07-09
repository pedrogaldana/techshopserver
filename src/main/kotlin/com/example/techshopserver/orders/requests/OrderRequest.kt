package com.example.techshopserver.orders.requests

import jakarta.annotation.Nullable

data class OrderRequest(
    @field:Nullable
    val id: Long?,

    @field:Nullable
    val idProduct: Long?
)