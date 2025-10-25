package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.data.storage.CartStorage
import com.example.pasteleriamilssaboresandroid.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartStorage: CartStorage) {
    val cartItems: Flow<List<CartItem>> = cartStorage.itemsFlow

    suspend fun saveCart(items: List<CartItem>) {
        cartStorage.save(items)
    }
}

