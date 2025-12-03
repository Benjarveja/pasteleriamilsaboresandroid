package com.example.pasteleriamilssaboresandroid.di

import android.content.Context
import com.example.pasteleriamilssaboresandroid.data.local.TokenManager
import com.example.pasteleriamilssaboresandroid.data.network.ApiService
import com.example.pasteleriamilssaboresandroid.data.network.AuthInterceptor
import com.example.pasteleriamilssaboresandroid.data.network.NetworkConfig
import com.example.pasteleriamilssaboresandroid.data.repository.AuthRepository
import com.example.pasteleriamilssaboresandroid.data.repository.NetworkAuthRepository
import com.example.pasteleriamilssaboresandroid.data.repository.NetworkCheckoutRepository
import com.example.pasteleriamilssaboresandroid.data.repository.NetworkOrderRepository
import com.example.pasteleriamilssaboresandroid.data.repository.NetworkProductRepository
import com.example.pasteleriamilssaboresandroid.data.repository.ProductRepository
import com.example.pasteleriamilssaboresandroid.ui.checkout.CheckoutViewModelFactory
import com.example.pasteleriamilssaboresandroid.viewmodel.OrdersViewModelFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppModule(private val appContext: Context) {

    private val tokenManager by lazy { TokenManager(appContext) }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()
    }

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(NetworkConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val authRepository: AuthRepository by lazy { NetworkAuthRepository(apiService) }

    val productRepository: ProductRepository by lazy { NetworkProductRepository(apiService) }

    val checkoutRepository: NetworkCheckoutRepository by lazy { NetworkCheckoutRepository(apiService) }

    val ordersRepository: NetworkOrderRepository by lazy { NetworkOrderRepository(apiService) }

    val ordersScreenViewModelFactory: OrdersViewModelFactory by lazy {
        OrdersViewModelFactory(ordersRepository)
    }

    val checkoutViewModelFactory: CheckoutViewModelFactory by lazy {
        CheckoutViewModelFactory(checkoutRepository, authRepository)
    }
}
