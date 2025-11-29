package com.example.pasteleriamilssaboresandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilssaboresandroid.data.repository.AuthRepository
import com.example.pasteleriamilssaboresandroid.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginResult {
    object Idle : LoginResult()
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}

sealed class RegisterResult {
    object Idle : RegisterResult()
    object Success : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}

class UserViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginResult: StateFlow<LoginResult> = _loginResult.asStateFlow()

    private val _registerResult = MutableStateFlow<RegisterResult>(RegisterResult.Idle)
    val registerResult: StateFlow<RegisterResult> = _registerResult.asStateFlow()

    private val _loggedInUser = MutableStateFlow<User?>(null)
    val loggedInUser: StateFlow<User?> = _loggedInUser.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = authRepository.login(email, password)
                _loggedInUser.value = user
                _loginResult.value = LoginResult.Success
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        _loggedInUser.value = null
    }

    fun resetLoginState() {
        _loginResult.value = LoginResult.Idle
    }

    fun resetRegisterState() {
        _registerResult.value = RegisterResult.Idle
    }

    fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phone: String,
        run: String?,
        birthDate: String?,
        region: String?,
        comuna: String?,
        street: String?
    ) {
        viewModelScope.launch {
            try {
                val user = authRepository.register(
                    firstName,
                    lastName,
                    email,
                    password,
                    phone,
                    run,
                    birthDate,
                    region,
                    comuna,
                    street
                )
                _loggedInUser.value = user
                _registerResult.value = RegisterResult.Success
            } catch (e: Exception) {
                _registerResult.value = RegisterResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}
