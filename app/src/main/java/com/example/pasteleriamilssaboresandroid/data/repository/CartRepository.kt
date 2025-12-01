package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.domain.model.CartItem
import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    val cartItems: StateFlow<List<CartItem>>
    suspend fun saveCart(items: List<CartItem>)
}
