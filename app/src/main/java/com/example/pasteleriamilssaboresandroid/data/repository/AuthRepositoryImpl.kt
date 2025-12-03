package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.data.api.AuthApiService
import com.example.pasteleriamilssaboresandroid.data.local.TokenManager
import com.example.pasteleriamilssaboresandroid.data.network.ApiService
import com.example.pasteleriamilssaboresandroid.data.network.dto.AuthRequest
import com.example.pasteleriamilssaboresandroid.data.network.dto.RegisterRequest
import com.example.pasteleriamilssaboresandroid.data.network.dto.UserResponse
import com.example.pasteleriamilssaboresandroid.domain.model.User
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    private var cachedUser: User? = null

    override suspend fun login(email: String, password: String): User {
        val response = apiService.login(AuthRequest(email, password))
        tokenManager.token = response.token
        tokenManager.refreshToken = response.refreshToken
        val user = apiService.getMe().toDomain()
        cachedUser = user
        return user
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
        tokenManager.token = response.token
        tokenManager.refreshToken = response.refreshToken
        val user = apiService.getMe().toDomain()
        cachedUser = user
        return user
    }

    override suspend fun updateUser(user: User): User {
        val updated = apiService.updateUser(user.id, user)
        cachedUser = updated
        return updated
    }

    override suspend fun getCurrentUser(): User? {
        if (cachedUser == null) {
            // Intentamos pedir el usuario; si la API responde 401/403, limpiamos el token local.
            cachedUser = try {
                apiService.getMe().toDomain()
            } catch (e: HttpException) {
                if (e.code() == 401 || e.code() == 403) {
                    tokenManager.clear()
                }
                null
            }
        }
        return cachedUser
    }

    override suspend fun logout() {
        tokenManager.clear()
        cachedUser = null
    }

    private fun UserResponse.toDomain() = User(
        id = id,
        name = "$firstName $lastName".trim(),
        email = email,
        firstName = firstName,
        lastName = lastName,
        run = run,
        phone = phone,
        birthDate = birthDate,
        region = region,
        comuna = comuna,
        street = street,
        address = address
    )
}
