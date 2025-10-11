package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}
