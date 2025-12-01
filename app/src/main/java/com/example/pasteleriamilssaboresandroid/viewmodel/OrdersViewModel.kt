package com.example.pasteleriamilssaboresandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilssaboresandroid.domain.model.Order
import com.example.pasteleriamilssaboresandroid.data.repository.NetworkOrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OrdersUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class OrdersViewModel(private val orderRepository: NetworkOrderRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = orderRepository.getMyOrders()
            _uiState.value = result.fold(
                onSuccess = { OrdersUiState(orders = it, isLoading = false) },
                onFailure = { OrdersUiState(isLoading = false, error = it.message) }
            )
        }
    }
}
