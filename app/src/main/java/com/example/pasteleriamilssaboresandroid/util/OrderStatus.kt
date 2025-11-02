package com.example.pasteleriamilssaboresandroid.util

import com.example.pasteleriamilssaboresandroid.data.database.order.Order

enum class OrderStatus(val displayName: String) {
    CONFIRMED("Pedido Confirmado"),
    PREPARING("Preparando Pedido"),
    PREPARED("Pedido Preparado"),
    WAITING_FOR_DISPATCH("En espera de despacho"),
    READY_FOR_PICKUP("Listo para Retirar"),
    DELIVERED("Pedido Entregado")
}

fun getOrderStatus(order: Order): OrderStatus {
    val hours = (System.currentTimeMillis() - order.createdAt) / 3_600_000
    return when (hours) {
        0L -> OrderStatus.CONFIRMED
        1L -> OrderStatus.PREPARING
        2L -> OrderStatus.PREPARED
        3L -> if (order.deliveryOption == "delivery") OrderStatus.WAITING_FOR_DISPATCH else OrderStatus.READY_FOR_PICKUP
        else -> OrderStatus.DELIVERED
    }
}
