package com.example.pasteleriamilssaboresandroid.ui.cart

import androidx.lifecycle.ViewModel
import com.example.pasteleriamilssaboresandroid.domain.model.CartItem
import com.example.pasteleriamilssaboresandroid.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CartUiState(
    val items: List<CartItem> = emptyList(),
) {
    val itemCount: Int get() = items.sumOf { it.quantity }
    val subtotal: Int get() = items.sumOf { it.lineTotal }
    val total: Int get() = subtotal // aquí se podrían sumar envíos o descuentos
}

class CartViewModel : ViewModel() {
    private val _ui = MutableStateFlow(CartUiState())
    val ui: StateFlow<CartUiState> = _ui.asStateFlow()

    fun add(product: Product, qty: Int = 1) {
        _ui.update { state ->
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
            state.copy(items = newItems)
        }
    }

    fun increment(productId: String) {
        _ui.update { s -> s.copy(items = s.items.map { if (it.productId == productId) it.copy(quantity = it.quantity + 1) else it }) }
    }

    fun decrement(productId: String) {
        _ui.update { s ->
            val updated = s.items.map { if (it.productId == productId) it.copy(quantity = (it.quantity - 1).coerceAtLeast(0)) else it }
                .filter { it.quantity > 0 }
            s.copy(items = updated)
        }
    }

    fun remove(productId: String) {
        _ui.update { s -> s.copy(items = s.items.filterNot { it.productId == productId }) }
    }

    fun clear() { _ui.update { CartUiState() } }
}

