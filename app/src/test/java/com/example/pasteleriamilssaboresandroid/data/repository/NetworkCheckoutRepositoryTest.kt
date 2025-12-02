package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.data.network.ApiService
import com.example.pasteleriamilssaboresandroid.data.network.dto.CheckoutRequest
import com.example.pasteleriamilssaboresandroid.data.network.dto.OrderItemResponse
import com.example.pasteleriamilssaboresandroid.data.network.dto.OrderResponse
import com.example.pasteleriamilssaboresandroid.domain.model.CartItem
import com.example.pasteleriamilssaboresandroid.domain.model.Discounts
import com.example.pasteleriamilssaboresandroid.domain.model.Order
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class NetworkCheckoutRepositoryTest {

    private val apiService: ApiService = mockk()
    private val repository = NetworkCheckoutRepository(apiService)

    private fun sampleOrder() = Order(
        code = "TEMP",
        createdAt = "",
        status = null,
        items = listOf(CartItem(productId = "P1", name = "Cake", price = 1000, quantity = 2)),
        subtotal = 2000,
        total = 2200,
        deliveryOption = "delivery",
        address = "Street 123",
        branch = null,
        pickupDate = null,
        pickupTimeSlot = null,
        paymentMethod = "cash",
        notes = "",
        contactName = "Ana Pérez",
        contactEmail = "ana@example.com",
        contactPhone = "+56912345678",
        contactBirthDate = "1990-01-01",
        discounts = Discounts(
            couponCode = "SAVE10",
            couponValid = true,
            couponDiscount = 100,
            seniorEligible = false,
            seniorDiscount = 0,
            totalSavings = 100,
            age = 34
        )
    )

    @Test
    fun `checkout envia request mapeado y retorna orden`() = runTest {
        // Verifica que los datos del Order se transforman en CheckoutRequest y se recibe la orden creada.
        val requestSlot = slot<CheckoutRequest>()
        coEvery { apiService.checkout(capture(requestSlot)) } returns OrderResponse(
            id = "1",
            code = "ORD-1",
            subtotal = 2000,
            total = 2200,
            couponCode = "SAVE10",
            couponDiscount = 100,
            seniorDiscount = null,
            totalSavings = 100,
            deliveryOption = "delivery",
            address = "Street 123",
            region = null,
            comuna = null,
            branchId = null,
            branchLabel = null,
            pickupDate = null,
            pickupTimeSlot = null,
            pickupTimeSlotLabel = null,
            paymentMethod = "cash",
            paymentMethodLabel = null,
            notes = null,
            contactEmail = "ana@example.com",
            contactPhone = "+56912345678",
            contactName = "Ana Pérez",
            status = "PROCESSING",
            createdAt = "2024-01-01",
            updatedAt = null,
            statusUpdatedAt = null,
            deliveredAt = null,
            items = listOf(OrderItemResponse("P1", "Cake", 2, 1000))
        )

        val result = repository.checkout(sampleOrder())

        assertTrue(result.isSuccess)
        val request = requestSlot.captured
        assertEquals("Ana", request.firstName)
        assertEquals("Pérez", request.lastName)
        assertEquals("delivery", request.deliveryOption)
        assertEquals("SAVE10", request.couponCode)
        assertEquals("ORD-1", result.getOrThrow().code)
    }

    @Test
    fun `checkout convierte HttpException en fallo`() = runTest {
        // Asegura que los errores HTTP del backend se propaguen como Result failure.
        val httpException = HttpException(Response.error<Unit>(400, okhttp3.ResponseBody.create(null, "Bad Request")))
        coEvery { apiService.checkout(any()) } throws httpException

        val result = repository.checkout(sampleOrder())

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is Exception)
    }
}
