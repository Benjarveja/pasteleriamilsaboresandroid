package com.example.pasteleriamilssaboresandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilssaboresandroid.data.database.user.User
import com.example.pasteleriamilssaboresandroid.data.database.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

sealed class UpdateResult {
    object Idle : UpdateResult()
    object Success : UpdateResult()
    data class Error(val message: String) : UpdateResult()
}

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResult = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginResult: StateFlow<LoginResult> = _loginResult.asStateFlow()

    private val _registerResult = MutableStateFlow<RegisterResult>(RegisterResult.Idle)
    val registerResult: StateFlow<RegisterResult> = _registerResult.asStateFlow()

    private val _updateResult = MutableStateFlow<UpdateResult>(UpdateResult.Idle)
    val updateResult: StateFlow<UpdateResult> = _updateResult.asStateFlow()

    private val _loggedInUser = MutableStateFlow<User?>(null)
    val loggedInUser: StateFlow<User?> = _loggedInUser.asStateFlow()

    fun getUser(email: String): Flow<User> {
        return repository.getUser(email)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                repository.findByEmail(email)
            }
            if (user == null) {
                _loginResult.value = LoginResult.Error("User not found")
            } else if (user.passwordHash != password) { // Plain text password check for now
                _loginResult.value = LoginResult.Error("Invalid password")
            } else {
                _loggedInUser.value = user
                _loginResult.value = LoginResult.Success
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

    fun resetUpdateState() {
        _updateResult.value = UpdateResult.Idle
    }

    fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phone: String,
        run: String,
        birthDate: String,
        region: String,
        comuna: String,
        street: String
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val user = User(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        passwordHash = password, // Remember to hash the password in a real app
                        phone = phone,
                        run = run,
                        birthDate = birthDate,
                        region = region,
                        comuna = comuna,
                        street = street
                    )
                    repository.insert(user)
                }
                _registerResult.value = RegisterResult.Success
            } catch (e: Exception) {
                _registerResult.value = RegisterResult.Error("Failed to register user: ${e.message}")
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.updateUser(user)
                }
                _loggedInUser.value = user // Update logged in user state
                _updateResult.value = UpdateResult.Success
            } catch (e: Exception) {
                _updateResult.value = UpdateResult.Error("Failed to update user: ${e.message}")
            }
        }
    }

    fun updateUserProfilePicture(userId: Int, uri: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.updateUserProfilePicture(userId, uri)
                }
                _loggedInUser.update { it?.copy(profilePictureUri = uri) }
                _updateResult.value = UpdateResult.Success
            } catch (e: Exception) {
                _updateResult.value = UpdateResult.Error("Failed to update profile picture: ${e.message}")
            }
        }
    }
}
