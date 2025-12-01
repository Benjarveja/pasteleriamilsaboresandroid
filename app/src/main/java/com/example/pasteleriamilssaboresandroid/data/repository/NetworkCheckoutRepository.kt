package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.data.network.ApiService
import com.example.pasteleriamilssaboresandroid.data.network.dto.CartItemRequest
import com.example.pasteleriamilssaboresandroid.data.network.dto.CheckoutRequest
import com.example.pasteleriamilssaboresandroid.data.network.dto.OrderResponse
import com.example.pasteleriamilssaboresandroid.domain.model.CartItem
import com.example.pasteleriamilssaboresandroid.domain.model.Order
import retrofit2.HttpException
import java.io.IOException

class NetworkCheckoutRepository(private val apiService: ApiService) {
    suspend fun checkout(order: Order): Result<Order> = try {
        val response = apiService.checkout(order.toCheckoutRequest())
        Result.success(response.toDomain(order.items))
    } catch (error: Throwable) {
        Result.failure(mapError(error))
    }

    private fun Order.toCheckoutRequest(): CheckoutRequest = CheckoutRequest(
        items = items.toCartItemRequests(),
        firstName = contactName.substringBefore(" ").ifBlank { contactName },
        lastName = contactName.substringAfter(" ", contactName),
        email = contactEmail,
        phone = contactPhone,
        run = contactBirthDate,
        birthDate = contactBirthDate,
        deliveryOption = deliveryOption,
        street = address,
        region = null,
        comuna = null,
        address = address,
        branch = branch,
        pickupDate = pickupDate,
        pickupTimeSlot = pickupTimeSlot,
        paymentMethod = paymentMethod,
        notes = notes,
        couponCode = discounts?.couponCode
    )

    private fun OrderResponse.toDomain(items: List<CartItem>): Order = Order(
        code = code,
        createdAt = createdAt.orEmpty(),
        status = status,
        userId = null,
        items = items,
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

    private fun List<CartItem>.toCartItemRequests() = map {
        CartItemRequest(codigo = it.productId, cantidad = it.quantity)
    }

    private fun mapError(error: Throwable): Throwable = when (error) {
        is HttpException -> IOException(error.response()?.errorBody()?.string() ?: "Error ${error.code()}")
        is IOException -> error
        else -> IllegalStateException(error.localizedMessage ?: "Error inesperado")
    }
}
