package com.example.pasteleriamilssaboresandroid.data.network.dto

data class UserResponse(
    val id: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val run: String?,
    val phone: String?,
    val birthDate: String?,
    val region: String?,
    val comuna: String?,
    val street: String?,
    val address: String?
)

