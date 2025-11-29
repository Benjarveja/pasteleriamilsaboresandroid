package com.example.pasteleriamilssaboresandroid.data.network

import com.example.pasteleriamilssaboresandroid.data.network.dto.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/products")
    suspend fun getProducts(): List<ProductResponse>

    @GET("api/products/{codigo}")
    suspend fun getProductByCodigo(@Path("codigo") codigo: String): ProductResponse
}

