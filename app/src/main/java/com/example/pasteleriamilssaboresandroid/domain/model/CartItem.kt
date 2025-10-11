package com.example.pasteleriamilssaboresandroid.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val productId: String,
    val name: String,
    val price: Int,
    val image: String? = null,
    val category: String? = null,
    val quantity: Int = 1,
) {
    val lineTotal: Int get() = price * quantity
}
