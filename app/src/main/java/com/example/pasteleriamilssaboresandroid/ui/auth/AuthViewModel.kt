package com.example.pasteleriamilssaboresandroid.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilssaboresandroid.data.repository.AuthRepository
import com.example.pasteleriamilssaboresandroid.data.storage.AuthStorage
import com.example.pasteleriamilssaboresandroid.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

class AuthViewModel(
    private val repo: AuthRepository,
    private val storage: AuthStorage,
) : ViewModel() {
    private val _ui = MutableStateFlow(AuthUiState(isLoading = true))
    val ui: StateFlow<AuthUiState> = _ui.asStateFlow()

    init {
        // Cargar sesión persistida
        viewModelScope.launch {
            storage.sessionFlow.collect { u ->
                _ui.update { it.copy(user = u, isLoading = false, error = null) }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }
            runCatching { repo.login(email, password) }
                .onSuccess { u ->
                    _ui.update { it.copy(isLoading = false, user = u) }
                    storage.saveSession(u)
                }
                .onFailure { e ->
                    _ui.update { it.copy(isLoading = false, error = e.message ?: "Error") }
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            storage.saveSession(null)
            _ui.update { it.copy(user = null) }
        }
    }
}

class AuthViewModelFactory(
    private val repo: AuthRepository,
    private val storage: AuthStorage,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repo, storage) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

