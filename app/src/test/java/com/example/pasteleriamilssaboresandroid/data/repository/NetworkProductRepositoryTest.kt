package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.data.network.ApiService
import com.example.pasteleriamilssaboresandroid.data.network.dto.ProductResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class NetworkProductRepositoryTest {

    private val apiService: ApiService = mockk()
    private val repository = NetworkProductRepository(apiService)

    @Test
    fun `obtenerProductos mapea la respuesta del servicio`() = runTest {
        // Se espera que cada ProductResponse se convierta correctamente a Product.
        val response = listOf(
            ProductResponse(
                codigo = "P1",
                categoria = "Tortas",
                nombre = "Selva Negra",
                precio = 15000,
                descripcion = "Bizcocho y crema",
                popular = true,
                historia = "Receta clásica",
                imagenUrl = "http://example.com/p1.jpg"
            )
        )
        coEvery { apiService.getProducts() } returns response

        val result = repository.getProducts()

        assertEquals(1, result.size)
        assertEquals("P1", result.first().productId)
        assertEquals("Selva Negra", result.first().productName)
        assertEquals(15000, result.first().price)
    }

    @Test
    fun `obtenerProductos retorna lista vacia cuando el servicio no tiene datos`() = runTest {
        // Se espera que el repositorio devuelva una lista vacía sin lanzar errores.
        coEvery { apiService.getProducts() } returns emptyList()

        val result = repository.getProducts()

        assertTrue(result.isEmpty())
    }
}
