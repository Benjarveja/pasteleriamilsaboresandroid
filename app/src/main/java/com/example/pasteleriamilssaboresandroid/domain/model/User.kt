package com.example.pasteleriamilssaboresandroid.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val run: String? = null,
    val phone: String? = null,
    val birthDate: String? = null,
    val region: String? = null,
    val comuna: String? = null,
    val street: String? = null,
    val address: String? = null,
)
