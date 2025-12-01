package com.example.pasteleriamilssaboresandroid.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pasteleriamilssaboresandroid.data.repository.AuthRepository
import com.example.pasteleriamilssaboresandroid.data.repository.NetworkCheckoutRepository

class CheckoutViewModelFactory(
    private val checkoutRepository: NetworkCheckoutRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CheckoutViewModel(checkoutRepository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
