package com.example.techshopserver.orders.responses

import com.example.techshopserver.products.Product
import com.example.techshopserver.products.responses.ProductResponse
import java.math.BigDecimal
import java.util.*

class OrderResponse(
    var id: Long,
    var date: Date,
    var amount: BigDecimal,
    var status: String,
    var products: MutableSet<ProductResponse>?  = mutableSetOf()
)