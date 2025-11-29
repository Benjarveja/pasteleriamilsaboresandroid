package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.data.network.ApiService
import com.example.pasteleriamilssaboresandroid.data.network.toDomain
import com.example.pasteleriamilssaboresandroid.domain.model.Product

class NetworkProductRepository(private val apiService: ApiService) : ProductRepository {
    override suspend fun getProducts(): List<Product> {
        return apiService.getProducts().map { it.toDomain() }
    }
}

