package com.example.pasteleriamilssaboresandroid.data.network.dto

data class ProductResponse(
    val codigo: String,
    val categoria: String,
    val nombre: String,
    val precio: Int,
    val descripcion: String,
    val popular: Boolean,
    val historia: String,
    val imagenUrl: String
)

