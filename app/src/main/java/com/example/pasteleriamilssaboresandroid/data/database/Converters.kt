package com.example.pasteleriamilssaboresandroid.data.database

import androidx.room.TypeConverter
import com.example.pasteleriamilssaboresandroid.domain.model.CartItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromCartItemList(value: List<CartItem>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toCartItemList(value: String): List<CartItem> {
        return Json.decodeFromString(value)
    }
}
