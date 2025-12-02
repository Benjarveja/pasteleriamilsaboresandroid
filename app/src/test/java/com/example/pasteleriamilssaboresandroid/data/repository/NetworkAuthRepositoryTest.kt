package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.data.network.ApiService
import com.example.pasteleriamilssaboresandroid.data.network.dto.AuthRequest
import com.example.pasteleriamilssaboresandroid.data.network.dto.AuthResponse
import com.example.pasteleriamilssaboresandroid.domain.model.User
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NetworkAuthRepositoryTest {

    private val apiService: ApiService = mockk(relaxed = true)
    private val repository = NetworkAuthRepository(apiService)

    @Test
    fun `login cachea y retorna usuario`() = runTest {
        // Verifica que el login envía las credenciales y cachea al usuario autenticado.
        val requestSlot = slot<AuthRequest>()
        coEvery { apiService.login(capture(requestSlot)) } returns AuthResponse(
            token = "jwt",
            refreshToken = "refresh",
            email = "user@example.com",
            userId = "123",
            roles = emptyList()
        )

        val user = repository.login("user@example.com", "secret")

        assertEquals("123", user.id)
        assertEquals("user@example.com", user.email)
        assertEquals("user@example.com", requestSlot.captured.email)
        assertEquals("secret", requestSlot.captured.password)
        assertEquals(user, repository.getCurrentUser())
    }

    @Test
    fun `register almacena nombre completo`() = runTest {
        // Garantiza que el nombre y apellido se concatenan y quedan en caché tras registrarse.
        coEvery { apiService.register(any()) } returns AuthResponse(
            token = "jwt",
            refreshToken = "refresh",
            email = "new@example.com",
            userId = "abc",
            roles = emptyList()
        )

        val result = repository.register(
            firstName = "Ana",
            lastName = "Pérez",
            email = "new@example.com",
            password = "pwd",
            phone = "123",
            run = null,
            birthDate = null,
            region = null,
            comuna = null,
            street = null
        )

        assertEquals("Ana Pérez", result.name)
        assertEquals(result, repository.getCurrentUser())
    }

    @Test
    fun `updateUser refresca la cache`() = runTest {
        // Comprueba que al actualizar el usuario en el backend, la caché local se sincroniza.
        val updated = User(id = "1", name = "Name", email = "mail@example.com")
        coEvery { apiService.updateUser("1", updated) } returns updated

        val result = repository.updateUser(updated)

        assertEquals(updated, result)
        assertEquals(updated, repository.getCurrentUser())
    }

    @Test
    fun `logout limpia la cache`() = runTest {
        // Asegura que la sesión en memoria se elimina al cerrar sesión.
        coEvery { apiService.login(any()) } returns AuthResponse(
            token = "jwt",
            refreshToken = "refresh",
            email = "user@example.com",
            userId = "123",
            roles = emptyList()
        )
        repository.login("user@example.com", "secret")

        repository.logout()

        assertNull(repository.getCurrentUser())
    }
}
