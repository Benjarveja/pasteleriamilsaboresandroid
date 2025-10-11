package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User
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

