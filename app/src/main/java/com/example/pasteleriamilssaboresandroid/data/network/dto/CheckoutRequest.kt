package com.example.pasteleriamilssaboresandroid.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutRequest(
    val items: List<CartItemRequest>,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val run: String? = null,
    val birthDate: String? = null,
    val deliveryOption: String,
    val street: String? = null,
    val region: String? = null,
    val comuna: String? = null,
    val address: String? = null,
    val branch: String? = null,
    val pickupDate: String? = null,
    val pickupTimeSlot: String? = null,
    val paymentMethod: String,
    val notes: String? = null,
    val couponCode: String? = null
)

