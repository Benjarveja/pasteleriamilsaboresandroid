package com.example.pasteleriamilssaboresandroid.di

import android.content.Context
import com.example.pasteleriamilssaboresandroid.data.api.AuthApiService
import com.example.pasteleriamilssaboresandroid.data.database.AppDatabase
import com.example.pasteleriamilssaboresandroid.data.database.order.OrderRepository as DatabaseOrderRepository
import com.example.pasteleriamilssaboresandroid.data.network.ApiService
import com.example.pasteleriamilssaboresandroid.data.network.NetworkConfig
import com.example.pasteleriamilssaboresandroid.data.repository.AuthRepository
import com.example.pasteleriamilssaboresandroid.data.repository.AuthRepositoryImpl
import com.example.pasteleriamilssaboresandroid.data.repository.CartRepository
import com.example.pasteleriamilssaboresandroid.data.repository.InMemoryCartRepository
import com.example.pasteleriamilssaboresandroid.data.repository.NetworkCheckoutRepository
import com.example.pasteleriamilssaboresandroid.data.repository.NetworkOrderRepository
import com.example.pasteleriamilssaboresandroid.data.repository.NetworkProductRepository
import com.example.pasteleriamilssaboresandroid.data.repository.ProductRepository
import java.util.concurrent.TimeUnit
import com.example.pasteleriamilssaboresandroid.data.local.TokenManager
import com.example.pasteleriamilssaboresandroid.data.network.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val authRepository: AuthRepository
    val productRepository: ProductRepository
    val checkoutRepository: NetworkCheckoutRepository
    val ordersRepository: NetworkOrderRepository
    val cartRepository: CartRepository
    val databaseOrderRepository: DatabaseOrderRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val baseUrl = NetworkConfig.baseUrl

    private val tokenManager: TokenManager by lazy { TokenManager(context) }

    private val okHttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(apiService, tokenManager)
    }

    override val productRepository: ProductRepository by lazy {
        NetworkProductRepository(apiService)
    }

    override val checkoutRepository: NetworkCheckoutRepository by lazy {
        NetworkCheckoutRepository(apiService)
    }

    override val ordersRepository: NetworkOrderRepository by lazy {
        NetworkOrderRepository(apiService)
    }

    override val cartRepository: CartRepository by lazy {
        InMemoryCartRepository()
    }

    override val databaseOrderRepository: DatabaseOrderRepository by lazy {
        val db = AppDatabase.getDatabase(context)
        DatabaseOrderRepository(db.orderDao())
    }
}
