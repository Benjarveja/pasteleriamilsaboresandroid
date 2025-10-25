package com.example.pasteleriamilssaboresandroid.data.database.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val passwordHash: String,
    val phone: String,
    val run: String,
    val birthDate: String,
    val region: String,
    val comuna: String,
    val street: String
)

