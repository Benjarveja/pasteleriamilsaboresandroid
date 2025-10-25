package com.example.pasteleriamilssaboresandroid.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilssaboresandroid.data.repository.CartRepository
import com.example.pasteleriamilssaboresandroid.domain.model.CartItem
import com.example.pasteleriamilssaboresandroid.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartUiState(
    val items: List<CartItem> = emptyList(),
) {
    val itemCount: Int get() = items.sumOf { it.quantity }
    val subtotal: Int get() = items.sumOf { it.lineTotal }
    val total: Int get() = subtotal // aquí se podrían sumar envíos o descuentos
}

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    private val _ui = MutableStateFlow(CartUiState())
    val ui: StateFlow<CartUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            repository.cartItems.collect { persistedItems ->
                _ui.update { it.copy(items = persistedItems) }
            }
        }
    }

    private fun setItems(newItems: List<CartItem>) {
        _ui.update { it.copy(items = newItems) }
        viewModelScope.launch { repository.saveCart(newItems) }
    }

    fun add(product: Product, qty: Int = 1) {
        val state = _ui.value
        val existing = state.items.find { it.productId == product.id }
        val newItems = if (existing != null) {
            state.items.map { if (it.productId == product.id) it.copy(quantity = it.quantity + qty) else it }
        } else {
            state.items + CartItem(
                productId = product.id,
                name = product.name,
                price = product.price,
                image = product.image,
                category = product.category,
                quantity = qty
            )
        }
        setItems(newItems)
    }

    fun increment(productId: String) {
        val newItems = _ui.value.items.map { if (it.productId == productId) it.copy(quantity = it.quantity + 1) else it }
        setItems(newItems)
    }

    fun decrement(productId: String) {
        val updated = _ui.value.items
            .map { if (it.productId == productId) it.copy(quantity = (it.quantity - 1).coerceAtLeast(0)) else it }
            .filter { it.quantity > 0 }
        setItems(updated)
    }

    fun remove(productId: String) {
        val newItems = _ui.value.items.filterNot { it.productId == productId }
        setItems(newItems)
    }

    fun clear() { setItems(emptyList()) }
}

class CartViewModelFactory(private val repository: CartRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
