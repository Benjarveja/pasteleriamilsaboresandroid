package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.data.network.ApiService
import com.example.pasteleriamilssaboresandroid.data.network.dto.AuthRequest
import com.example.pasteleriamilssaboresandroid.data.network.dto.RegisterRequest
import com.example.pasteleriamilssaboresandroid.domain.model.User

class NetworkAuthRepository(private val apiService: ApiService) : AuthRepository {
    private var cachedUser: User? = null

    override suspend fun login(email: String, password: String): User {
        val response = apiService.login(AuthRequest(email, password))
        return User(
            id = response.userId,
            email = response.email,
            name = ""
        ).also { cachedUser = it }
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
        return User(
            id = response.userId,
            email = response.email,
            name = "$firstName $lastName"
        ).also { cachedUser = it }
    }

    override suspend fun updateUser(user: User): User {
        return apiService.updateUser(user.id, user).also { cachedUser = it }
    }

    override suspend fun getCurrentUser(): User? = cachedUser

    override suspend fun logout() {
        cachedUser = null
    }
}
