package com.example.pasteleriamilssaboresandroid.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderItemResponse(
    val codigo: String,
    val nombre: String,
    val cantidad: Int,
    val precio: Int
)

@Serializable
data class OrderResponse(
    val id: String,
    val code: String,
    val subtotal: Int,
    val total: Int,
    val couponCode: String?,
    val couponDiscount: Int?,
    val seniorDiscount: Int?,
    val totalSavings: Int?,
    val deliveryOption: String?,
    val address: String?,
    val region: String?,
    val comuna: String?,
    val branchId: String?,
    val branchLabel: String?,
    val pickupDate: String?,
    val pickupTimeSlot: String?,
    val pickupTimeSlotLabel: String?,
    val paymentMethod: String?,
    val paymentMethodLabel: String?,
    val notes: String?,
    val contactEmail: String?,
    val contactPhone: String?,
    val contactName: String?,
    val status: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val statusUpdatedAt: String?,
    val deliveredAt: String?,
    val items: List<OrderItemResponse> = emptyList()
)

