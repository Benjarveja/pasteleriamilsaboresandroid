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

class DemoAuthRepository : AuthRepository {
    private val demoEmail = "cliente@milsabores.cl"
    private val demoPassword = "MilSabores123"
    private val demoUser = User(
        id = "demo-user",
        name = "Fernanda Donoso",
        email = demoEmail,
    )

    override suspend fun login(email: String, password: String): User {
        val normalized = email.trim().lowercase()
        if (normalized == demoEmail && password == demoPassword) return demoUser
        throw IllegalArgumentException("Credenciales inválidas. Revisa tu correo y contraseña.")
    }
}

