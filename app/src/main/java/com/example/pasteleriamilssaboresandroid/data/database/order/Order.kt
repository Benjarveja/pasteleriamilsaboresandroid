package com.example.pasteleriamilssaboresandroid.data.database.order

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pasteleriamilssaboresandroid.domain.model.CartItem

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val code: String,
    val createdAt: Long,
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
    val contactBirthDate: String?
)
