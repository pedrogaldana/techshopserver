package com.example.techshopserver.users.responses

data class LoginResponse(
    val token: String,
    val user: UserResponse
)
