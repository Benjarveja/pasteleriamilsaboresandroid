package com.example.pasteleriamilssaboresandroid.data.network

import com.example.pasteleriamilssaboresandroid.data.local.TokenManager
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenManager.token

        // Si hay token y la ruta lo requiere, lo añadimos; si no, dejamos pasar la petición sin Authorization.
        val requestToProceed = if (!token.isNullOrBlank() && shouldAttachToken(originalRequest.url)) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        val response = chain.proceed(requestToProceed)
        // Si el backend responde con no autorizado, limpiamos el token local para forzar re-login.
        if (response.code == 401 || response.code == 403) {
            tokenManager.clear()
        }
        return response
    }

    private fun shouldAttachToken(url: HttpUrl): Boolean {
        val path = url.encodedPath
        // No agregar token para endpoints públicos como auth (login/register) o products (lista y detalle).
        // Se asume que las rutas del ApiService son: api/auth, api/products, etc.
        return !(path.startsWith("/api/auth") || path.startsWith("/api/products"))
    }
}
