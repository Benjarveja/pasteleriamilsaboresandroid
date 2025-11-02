package com.example.pasteleriamilssaboresandroid.domain.model

data class Order(
    val code: String,
    val createdAt: String,
    val userId: String? = null,
    val items: List<CartItem>,
    val subtotal: Int,
    val total: Int,
    val deliveryOption: String,
    val address: String?,
    val branch: String?,
    val pickupDate: String?,
    val pickupTimeSlot: String?,
    val paymentMethod: String,
    val notes: String?,
    val contactName: String,
    val contactEmail: String,
    val contactPhone: String,
    val contactBirthDate: String?,
    val discounts: Discounts?
)

data class Discounts(
    val couponCode: String?,
    val couponValid: Boolean,
    val couponDiscount: Int,
    val seniorEligible: Boolean,
    val seniorDiscount: Int,
    val totalSavings: Int,
    val age: Int?
)
