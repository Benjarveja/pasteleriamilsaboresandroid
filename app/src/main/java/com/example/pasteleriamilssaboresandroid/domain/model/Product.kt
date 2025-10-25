package com.example.pasteleriamilssaboresandroid.domain.model

data class Product(
    val productId: String,
    val productName: String,
    val price: Int,
    val category: String,
    val productImage: String? = null,
    val productDescription: String? = null,
    val popular: Boolean = false,
    val history: String? = null,
) : ContentItem(productId, productName, productImage, productDescription)
