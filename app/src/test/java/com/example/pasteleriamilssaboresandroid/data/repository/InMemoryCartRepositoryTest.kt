package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.domain.model.CartItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class InMemoryCartRepositoryTest {

    private val repository = InMemoryCartRepository()

    @Test
    fun `cartItems emite lista vacia por defecto`() = runTest {
        // Al inicializar el repositorio no debe haber ítems almacenados.
        assertEquals(emptyList<CartItem>(), repository.cartItems.first())
    }

    @Test
    fun `saveCart actualiza el estado`() = runTest {
        // Guardar un carrito debe reflejarse inmediatamente en el flujo de estados.
        val items = listOf(CartItem("P1", "Cake", price = 1000, quantity = 1))

        repository.saveCart(items)

        assertEquals(items, repository.cartItems.first())
    }

    @Test
    fun `saveCart reemplaza el estado anterior`() = runTest {
        // Persistir un segundo carrito debe sobreescribir el contenido previo.
        val first = listOf(CartItem("P1", "Cake", 1000, quantity = 1))
        val second = listOf(CartItem("P2", "Pie", 2000, quantity = 3))

        repository.saveCart(first)
        repository.saveCart(second)

        assertEquals(second, repository.cartItems.first())
    }
}
