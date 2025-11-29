package com.example.pasteleriamilssaboresandroid.data.network.dto

data class AuthResponse(
    val token: String,
    val refreshToken: String,
    val email: String,
    val userId: String,
    val roles: List<String>
)

