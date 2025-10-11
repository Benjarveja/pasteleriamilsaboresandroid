package com.example.pasteleriamilssaboresandroid.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilssaboresandroid.data.repository.InMemoryProductRepository
import com.example.pasteleriamilssaboresandroid.data.repository.ProductRepository
import com.example.pasteleriamilssaboresandroid.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// UI State para la pantalla de productos
data class ProductsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val products: List<Product> = emptyList(),
    val query: String = "",
    val category: String? = null,
    val categories: List<String> = emptyList(),
    val minPrice: Int = 0,
    val maxPrice: Int = 0,
    val selectedMinPrice: Int = 0,
    val selectedMaxPrice: Int = 0,
)

class ProductsViewModel(
    private val repository: ProductRepository = InMemoryProductRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState(isLoading = true))
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { repository.getProducts() }
                .onSuccess { list ->
                    val categories = list.map { it.category }.distinct()
                    val minPrice = list.minOfOrNull { it.price } ?: 0
                    val maxPrice = list.maxOfOrNull { it.price } ?: 0
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            products = list,
                            categories = categories,
                            minPrice = minPrice,
                            maxPrice = maxPrice,
                            selectedMinPrice = minPrice,
                            selectedMaxPrice = maxPrice
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error inesperado") }
                }
        }
    }

    fun onQueryChange(newQuery: String) { _uiState.update { it.copy(query = newQuery) } }

    fun onCategoryChange(newCategory: String?) { _uiState.update { it.copy(category = newCategory) } }

    fun onPriceRangeChange(min: Int, max: Int) {
        _uiState.update { state ->
            val clippedMin = min.coerceAtLeast(state.minPrice)
            val clippedMax = max.coerceAtMost(state.maxPrice)
            state.copy(selectedMinPrice = clippedMin, selectedMaxPrice = clippedMax)
        }
    }

    fun resetFilters() {
        _uiState.update { s -> s.copy(query = "", category = null, selectedMinPrice = s.minPrice, selectedMaxPrice = s.maxPrice) }
    }

    fun filtered(state: ProductsUiState = _uiState.value): List<Product> {
        val q = state.query.trim().lowercase()
        return state.products.filter { p ->
            (q.isEmpty() || p.name.lowercase().contains(q) || p.category.lowercase().contains(q)) &&
            (state.category == null || state.category == p.category) &&
            (p.price in state.selectedMinPrice..state.selectedMaxPrice)
        }
    }
}
