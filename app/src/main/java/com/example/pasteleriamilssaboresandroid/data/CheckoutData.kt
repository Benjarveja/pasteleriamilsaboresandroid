package com.example.pasteleriamilssaboresandroid.data

data class PaymentMethod(val id: String, val label: String)
data class PickupBranch(val id: String, val name: String, val address: String)
data class PickupTimeSlot(val id: String, val label: String)

val paymentMethods = listOf(
    PaymentMethod("transfer", "Transferencia o depósito bancario"),
    PaymentMethod("webpay", "Webpay (tarjetas de crédito o débito)"),
)

val pickupBranches = listOf(
    PickupBranch("providencia", "Providencia", "Av. Providencia 123, Santiago"),
    PickupBranch("vitacura", "Vitacura", "Av. Vitacura 456, Santiago"),
    PickupBranch("las-condes", "Las Condes", "Av. Las Condes 789, Santiago"),
)

val pickupTimeSlots = listOf(
    PickupTimeSlot("09-11", "Entre 09:00 y 11:00"),
    PickupTimeSlot("11-13", "Entre 11:00 y 13:00"),
    PickupTimeSlot("13-15", "Entre 13:00 y 15:00"),
    PickupTimeSlot("15-17", "Entre 15:00 y 17:00"),
)
