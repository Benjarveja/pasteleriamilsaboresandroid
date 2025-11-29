package com.example.pasteleriamilssaboresandroid.data.network

import com.example.pasteleriamilssaboresandroid.data.network.dto.AuthRequest
import com.example.pasteleriamilssaboresandroid.data.network.dto.AuthResponse
import com.example.pasteleriamilssaboresandroid.data.network.dto.ProductResponse
import com.example.pasteleriamilssaboresandroid.data.network.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("api/products")
    suspend fun getProducts(): List<ProductResponse>

    @GET("api/products/{codigo}")
    suspend fun getProductByCodigo(@Path("codigo") codigo: String): ProductResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}

