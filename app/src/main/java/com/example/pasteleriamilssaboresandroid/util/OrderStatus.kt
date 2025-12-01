package com.example.pasteleriamilssaboresandroid.util

import com.example.pasteleriamilssaboresandroid.domain.model.Order

enum class OrderStatus(val displayName: String) {
    CONFIRMADO("Pedido confirmado"),
    EN_PREPARACION("Preparando pedido"),
    PEDIDO_PREPARADO("Pedido preparado"),
    EN_ESPERA_DESPACHO("En espera de despacho"),
    LISTO_PARA_RETIRAR("Listo para retirar"),
    ENTREGADO("Pedido entregado"),
    UNKNOWN("Estado desconocido")
}

fun getOrderStatus(order: Order): OrderStatus = when (order.status?.uppercase()) {
    "CONFIRMADO" -> OrderStatus.CONFIRMADO
    "EN_PREPARACION" -> OrderStatus.EN_PREPARACION
    "PEDIDO_PREPARADO" -> OrderStatus.PEDIDO_PREPARADO
    "EN_ESPERA_DESPACHO" -> OrderStatus.EN_ESPERA_DESPACHO
    "LISTO_PARA_RETIRAR" -> OrderStatus.LISTO_PARA_RETIRAR
    "ENTREGADO" -> OrderStatus.ENTREGADO
    else -> OrderStatus.UNKNOWN
}
