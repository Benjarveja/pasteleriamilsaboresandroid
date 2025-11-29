package com.example.pasteleriamilssaboresandroid.data.network.dto

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val run: String?,
    val street: String?,
    val region: String?,
    val comuna: String?,
    val birthDate: String?
)

