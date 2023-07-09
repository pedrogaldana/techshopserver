package com.example.techshopserver

import com.example.techshopserver.products.Product
import com.example.techshopserver.users.Role
import com.example.techshopserver.users.User
import kotlin.random.Random

fun randomString(length: Int = 10, allowedChars: List<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')) =
    (1..length).map { allowedChars.random() }.joinToString()

object Stubs {
    fun userStub(
        id: Long? = Random.nextLong(1, 1000),
        roles: List<String> = listOf("OPERATOR")
    ): User {
        val name = "user-${id ?: "new"}"
        return User(
            id = id,
            name = name,
            email = "$name@email.com",
            password = randomString(),
            roles = roles.mapIndexed { i, it -> Role(i.toLong(), it) }.toMutableSet()
        )
    }

    fun productStub(
        id: Long? = Random.nextLong(1, 1000),
    ): Product {
        val name = "product-${id ?: "new"}"
        val description = "description-$name"
        val price = Random.nextInt(1, 9999).toBigDecimal()
        val category = "TESTE"
        return Product(
            id = id,
            name = name,
            price = price,
            description = description,
            category = category
        )
    }
}