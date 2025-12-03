package com.example.pasteleriamilssaboresandroid.data.network

import com.example.pasteleriamilssaboresandroid.data.network.dto.AuthRequest
import com.example.pasteleriamilssaboresandroid.data.network.dto.AuthResponse
import com.example.pasteleriamilssaboresandroid.data.network.dto.ProductResponse
import com.example.pasteleriamilssaboresandroid.data.network.dto.RegisterRequest
import com.example.pasteleriamilssaboresandroid.data.network.dto.CheckoutRequest
import com.example.pasteleriamilssaboresandroid.domain.model.User
import com.example.pasteleriamilssaboresandroid.data.network.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<ProductResponse>

    @GET("products/{codigo}")
    suspend fun getProductByCodigo(@Path("codigo") codigo: String): ProductResponse

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): User

    @POST("checkout")
    suspend fun checkout(@Body request: CheckoutRequest): com.example.pasteleriamilssaboresandroid.data.network.dto.OrderResponse

    @GET("orders/me")
    suspend fun getMyOrders(): List<com.example.pasteleriamilssaboresandroid.data.network.dto.OrderResponse>

    @GET("users/me")
    suspend fun getMe(): UserResponse
}
