package com.example.pasteleriamilssaboresandroid.domain.model

data class Product(
    val id: String,
    val name: String,
    val price: Int,
    val category: String,
    val image: String? = null,
    val description: String? = null,
    val popular: Boolean = false,
    val history: String? = null,
)
