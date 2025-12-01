package com.example.pasteleriamilssaboresandroid.util

import com.example.pasteleriamilssaboresandroid.domain.model.Order

enum class OrderStatus(val displayName: String) {
    CONFIRMED("Pedido Confirmado"),
    PREPARING("Preparando Pedido"),
    PREPARED("Pedido Preparado"),
    WAITING_FOR_DISPATCH("En espera de despacho"),
    READY_FOR_PICKUP("Listo para Retirar"),
    DELIVERED("Pedido Entregado"),
    UNKNOWN("Estado desconocido")
}

fun getOrderStatus(order: Order): OrderStatus = when (order.status?.uppercase()) {
    "CONFIRMED" -> OrderStatus.CONFIRMED
    "PREPARING" -> OrderStatus.PREPARING
    "PREPARED" -> OrderStatus.PREPARED
    "WAITING_FOR_DISPATCH" -> OrderStatus.WAITING_FOR_DISPATCH
    "READY_FOR_PICKUP" -> OrderStatus.READY_FOR_PICKUP
    "DELIVERED" -> OrderStatus.DELIVERED
    else -> OrderStatus.UNKNOWN
}
