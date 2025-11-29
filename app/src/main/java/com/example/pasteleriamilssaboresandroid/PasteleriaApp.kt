package com.example.pasteleriamilssaboresandroid

import android.app.Application
import com.example.pasteleriamilssaboresandroid.data.database.AppDatabase
import com.example.pasteleriamilssaboresandroid.data.database.order.OrderRepository
import com.example.pasteleriamilssaboresandroid.data.database.user.UserRepository
import com.example.pasteleriamilssaboresandroid.data.network.RetrofitClient
import com.example.pasteleriamilssaboresandroid.data.repository.CartRepository
import com.example.pasteleriamilssaboresandroid.data.repository.NetworkProductRepository
import com.example.pasteleriamilssaboresandroid.data.repository.ProductRepository
import com.example.pasteleriamilssaboresandroid.data.storage.CartStorage

class PasteleriaApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    import com.example.pasteleriamilssaboresandroid.data.repository.AuthRepository
import com.example.pasteleriamilssaboresandroid.data.repository.NetworkAuthRepository
// ... existing code ...
    val orderRepository by lazy { OrderRepository(database.orderDao()) }
    val cartRepository by lazy { CartRepository(CartStorage(this)) }
    val productRepository: ProductRepository by lazy {
        NetworkProductRepository(RetrofitClient.instance)
    }
    val authRepository: AuthRepository by lazy {
        NetworkAuthRepository(RetrofitClient.instance)
    }
}
