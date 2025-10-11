package com.example.pasteleriamilssaboresandroid.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilssaboresandroid.data.repository.AssetsNewsRepository
import com.example.pasteleriamilssaboresandroid.domain.model.NewsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NewsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val items: List<NewsItem> = emptyList()
)

class NewsViewModel(private val repo: AssetsNewsRepository) : ViewModel() {
    private val _ui = MutableStateFlow(NewsUiState(isLoading = true))
    val ui: StateFlow<NewsUiState> = _ui.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }
            runCatching { repo.getNews() }
                .onSuccess { list -> _ui.update { it.copy(isLoading = false, items = list) } }
                .onFailure { e -> _ui.update { it.copy(isLoading = false, error = e.message ?: "Error") } }
        }
    }
}

