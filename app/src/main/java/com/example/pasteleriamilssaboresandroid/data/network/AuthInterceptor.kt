package com.example.pasteleriamilssaboresandroid.data.network

import com.example.pasteleriamilssaboresandroid.data.local.TokenManager
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenManager.token

        val requestToProceed = if (token != null && shouldAttachToken(originalRequest.url)) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        val response = chain.proceed(requestToProceed)
        if (response.code == 401 || response.code == 403) {
            tokenManager.clear()
        }
        return response
    }

    private fun shouldAttachToken(url: HttpUrl): Boolean {
        val path = url.encodedPath
        return !path.startsWith("/api/auth") && !path.startsWith("/api/products")
    }
}
