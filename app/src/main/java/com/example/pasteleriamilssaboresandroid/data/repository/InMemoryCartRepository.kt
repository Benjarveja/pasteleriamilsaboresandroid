package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.domain.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryCartRepository : CartRepository {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    override val cartItems: StateFlow<List<CartItem>> = _items.asStateFlow()

    override suspend fun saveCart(items: List<CartItem>) {
        _items.value = items
    }

    // helpers (no forman parte de la interfaz, pero pueden usarse internamente si se necesita)
    @Suppress("unused")
    private fun addItem(item: CartItem) {
        val current = _items.value
        val existing = current.find { it.productId == item.productId }
        val newList = if (existing != null) {
            current.map {
                if (it.productId == item.productId) it.copy(quantity = it.quantity + item.quantity) else it
            }
        } else {
            current + item
        }
        _items.value = newList
    }

    @Suppress("unused")
    private fun removeItem(item: CartItem) {
        val current = _items.value
        val newList = current.mapNotNull {
            if (it.productId == item.productId) {
                if (it.quantity > item.quantity) it.copy(quantity = it.quantity - item.quantity) else null
            } else it
        }
        _items.value = newList
    }

    @Suppress("unused")
    private fun clear() {
        _items.value = emptyList()
    }
}
