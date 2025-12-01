package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.data.network.ApiService
import com.example.pasteleriamilssaboresandroid.data.network.dto.OrderResponse
import com.example.pasteleriamilssaboresandroid.domain.model.CartItem
import com.example.pasteleriamilssaboresandroid.domain.model.Order
import retrofit2.HttpException
import java.io.IOException

class NetworkOrderRepository(private val apiService: ApiService) {
    suspend fun getMyOrders(): Result<List<Order>> = try {
        val orders = apiService.getMyOrders().map { it.toDomain() }
        Result.success(orders)
    } catch (error: Throwable) {
        Result.failure(mapError(error))
    }

    private fun OrderResponse.toDomain(): Order = Order(
        code = code,
        createdAt = createdAt.orEmpty(),
        status = status,
        userId = null,
        items = items.map {
            CartItem(
                productId = it.codigo,
                name = it.nombre,
                price = it.precio,
                quantity = it.cantidad
            )
        },
        subtotal = subtotal,
        total = total,
        deliveryOption = deliveryOption.orEmpty(),
        address = address,
        branch = branchLabel,
        pickupDate = pickupDate,
        pickupTimeSlot = pickupTimeSlot,
        paymentMethod = paymentMethod.orEmpty(),
        notes = notes,
        contactName = contactName.orEmpty(),
        contactEmail = contactEmail.orEmpty(),
        contactPhone = contactPhone.orEmpty(),
        contactBirthDate = null,
        discounts = null
    )

    private fun mapError(error: Throwable): Throwable = when (error) {
        is HttpException -> IOException(error.response()?.errorBody()?.string() ?: "Error ${error.code()}")
        is IOException -> error
        else -> IllegalStateException(error.localizedMessage ?: "Error inesperado")
    }
}
