package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.data.network.ApiService
import com.example.pasteleriamilssaboresandroid.data.network.dto.OrderItemResponse
import com.example.pasteleriamilssaboresandroid.data.network.dto.OrderResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class NetworkOrderRepositoryTest {

    private val apiService: ApiService = mockk()
    private val repository = NetworkOrderRepository(apiService)

    private fun sampleResponse() = OrderResponse(
        id = "1",
        code = "ORD-1",
        subtotal = 1000,
        total = 1200,
        couponCode = null,
        couponDiscount = null,
        seniorDiscount = null,
        totalSavings = null,
        deliveryOption = "delivery",
        address = "Street",
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
        contactEmail = "user@example.com",
        contactPhone = "+56912312312",
        contactName = "Ana",
        status = "PROCESSING",
        createdAt = "2024-01-01",
        updatedAt = null,
        statusUpdatedAt = null,
        deliveredAt = null,
        items = listOf(OrderItemResponse("P1", "Cake", 1, 1000))
    )

    @Test
    fun `getMyOrders retorna ordenes mapeadas`() = runTest {
        // Comprueba que la respuesta del backend se convierte en modelo de dominio con sus items.
        coEvery { apiService.getMyOrders() } returns listOf(sampleResponse())

        val result = repository.getMyOrders()

        assertTrue(result.isSuccess)
        val orders = result.getOrThrow()
        assertEquals(1, orders.size)
        assertEquals("ORD-1", orders.first().code)
        assertEquals(1, orders.first().items.size)
        assertEquals("Cake", orders.first().items.first().name)
    }

    @Test
    fun `getMyOrders propaga errores`() = runTest {
        // Valida que una HttpException se traduzca en un Result failure.
        val httpException = HttpException(Response.error<Unit>(403, okhttp3.ResponseBody.create(null, "Forbidden")))
        coEvery { apiService.getMyOrders() } throws httpException

        val result = repository.getMyOrders()

        assertTrue(result.isFailure)
    }
}
