package com.example.pasteleriamilssaboresandroid.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilssaboresandroid.data.repository.AssetsProductRepository
import com.example.pasteleriamilssaboresandroid.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val product: Product? = null,
)

class ProductDetailViewModel(
    private val repo: AssetsProductRepository,
    private val productId: String,
) : ViewModel() {
    private val _ui = MutableStateFlow(ProductDetailUiState(isLoading = true))
    val ui: StateFlow<ProductDetailUiState> = _ui.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }
            runCatching {
                val all = repo.getProducts()
                all.firstOrNull { it.id == productId } ?: error("Producto no encontrado")
            }.onSuccess { p ->
                _ui.update { it.copy(isLoading = false, product = p) }
            }.onFailure { e ->
                _ui.update { it.copy(isLoading = false, error = e.message ?: "Error") }
            }
        }
    }
}

class ProductDetailViewModelFactory(
    private val repo: AssetsProductRepository,
    private val productId: String,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            return ProductDetailViewModel(repo, productId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
