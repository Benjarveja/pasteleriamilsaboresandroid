package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User
    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phone: String,
        run: String?,
        birthDate: String?,
        region: String?,
        comuna: String?,
        street: String?
    ): User
}



