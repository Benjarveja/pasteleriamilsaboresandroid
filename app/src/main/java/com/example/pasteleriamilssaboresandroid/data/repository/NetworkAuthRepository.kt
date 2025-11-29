package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.data.network.ApiService
import com.example.pasteleriamilssaboresandroid.data.network.dto.AuthRequest
import com.example.pasteleriamilssaboresandroid.data.network.dto.RegisterRequest
import com.example.pasteleriamilssaboresandroid.domain.model.User

class NetworkAuthRepository(private val apiService: ApiService) : AuthRepository {
    override suspend fun login(email: String, password: String): User {
        val response = apiService.login(AuthRequest(email, password))
        // Aquí puedes guardar el token y refreshToken si es necesario
        return User(
            id = response.userId,
            email = response.email,
            name = "" // El backend no devuelve el nombre en AuthResponse
        )
    }

    override suspend fun register(
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
    ): User {
        val response = apiService.register(
            RegisterRequest(
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                run = run,
                street = street,
                region = region,
                comuna = comuna,
                birthDate = birthDate
            )
        )
        // Aquí puedes guardar el token y refreshToken si es necesario
        return User(
            id = response.userId,
            email = response.email,
            name = "$firstName $lastName"
        )
    }
}

