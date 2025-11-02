package com.example.pasteleriamilssaboresandroid

import android.app.Application
import com.example.pasteleriamilssaboresandroid.data.database.AppDatabase
import com.example.pasteleriamilssaboresandroid.data.database.order.OrderRepository
import com.example.pasteleriamilssaboresandroid.data.database.user.UserRepository

import com.example.pasteleriamilssaboresandroid.data.repository.CartRepository
import com.example.pasteleriamilssaboresandroid.data.storage.CartStorage

class PasteleriaApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val orderRepository by lazy { OrderRepository(database.orderDao()) }
    val cartRepository by lazy { CartRepository(CartStorage(this)) }
}
