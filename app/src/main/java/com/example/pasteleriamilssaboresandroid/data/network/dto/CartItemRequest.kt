package com.example.pasteleriamilssaboresandroid.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CartItemRequest(
    val codigo: String,
    val cantidad: Int
)

