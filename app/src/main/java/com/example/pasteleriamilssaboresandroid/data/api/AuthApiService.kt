package com.example.pasteleriamilssaboresandroid.data.api

import com.example.pasteleriamilssaboresandroid.data.network.dto.AuthResponse
import com.example.pasteleriamilssaboresandroid.data.network.dto.RegisterRequest
import com.example.pasteleriamilssaboresandroid.domain.model.User
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body credentials: Map<String, String>): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @PUT("api/users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): User
}
