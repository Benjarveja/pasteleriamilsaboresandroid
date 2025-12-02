package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.data.local.TokenManager
import com.example.pasteleriamilssaboresandroid.data.network.ApiService
import com.example.pasteleriamilssaboresandroid.data.network.dto.AuthRequest
import com.example.pasteleriamilssaboresandroid.data.network.dto.AuthResponse
import com.example.pasteleriamilssaboresandroid.data.network.dto.RegisterRequest
import com.example.pasteleriamilssaboresandroid.data.network.dto.UserResponse
import com.example.pasteleriamilssaboresandroid.domain.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Test

@RunWith(JUnit4::class)
class AuthRepositoryImplTest {

    private val apiService: ApiService = mockk()
    private val tokenManager: TokenManager = mockk(relaxed = true)
    private val repository = AuthRepositoryImpl(apiService, tokenManager)

    private fun sampleUserResponse(
        id: String = "1",
        firstName: String = "Ana",
        lastName: String = "López"
    ) = UserResponse(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = "ana@example.com",
        run = "12.345.678-9",
        phone = "+56911111111",
        birthDate = "1990-01-01",
        region = "RM",
        comuna = "Santiago",
        street = "Calle 123",
        address = "Calle 123, Santiago"
    )

    @Test
    fun `login guarda tokens y usuario autenticado`() = runTest {
        // Debe enviar las credenciales, guardar los tokens y cachear al usuario retornado por getMe().
        coEvery { apiService.login(any()) } returns AuthResponse(
            token = "jwt-token",
            refreshToken = "refresh-token",
            email = "ana@example.com",
            userId = "1",
            roles = listOf("ROLE_USER")
        )
        coEvery { apiService.getMe() } returns sampleUserResponse()

        val user = repository.login("ana@example.com", "secreto")

        verify { tokenManager.token = "jwt-token" }
        verify { tokenManager.refreshToken = "refresh-token" }
        coVerify { apiService.getMe() }
        assertEquals("Ana López", user.name)
        assertEquals(user, repository.getCurrentUser())
    }

    @Test
    fun `register guarda tokens y concatena nombre completo`() = runTest {
        // Después de registrar se debe autenticar automáticamente y mantener el usuario en caché.
        coEvery { apiService.register(any()) } returns AuthResponse(
            token = "jwt-2",
            refreshToken = "refresh-2",
            email = "nuevo@example.com",
            userId = "2",
            roles = emptyList()
        )
        coEvery { apiService.getMe() } returns sampleUserResponse(id = "2", firstName = "Carla", lastName = "Pérez")

        val user = repository.register(
            firstName = "Carla",
            lastName = "Pérez",
            email = "nuevo@example.com",
            password = "clave",
            phone = "999999",
            run = null,
            birthDate = null,
            region = null,
            comuna = null,
            street = null
        )

        verify { tokenManager.token = "jwt-2" }
        verify { tokenManager.refreshToken = "refresh-2" }
        coVerify { apiService.getMe() }
        assertEquals("Carla Pérez", user.name)
        assertEquals(user, repository.getCurrentUser())
    }

    @Test
    fun `updateUser sincroniza la cache local`() = runTest {
        // Al actualizar el usuario, la instancia en memoria debe reflejar la versión del backend.
        val updated = User(id = "1", name = "Ana Actualizada", email = "ana@example.com")
        coEvery { apiService.updateUser("1", updated) } returns updated

        val result = repository.updateUser(updated)

        assertEquals(updated, result)
        assertEquals(updated, repository.getCurrentUser())
    }

    @Test
    fun `getCurrentUser consulta al backend solo cuando no hay cache`() = runTest {
        // La primera llamada debe invocar a getMe(); la segunda reutiliza la cache.
        coEvery { apiService.getMe() } returns sampleUserResponse()

        val primero = repository.getCurrentUser()
        val segundo = repository.getCurrentUser()

        coVerify(exactly = 1) { apiService.getMe() }
        assertNotNull(primero)
        assertEquals(primero, segundo)
    }

    @Test
    fun `logout limpia tokens y fuerza nueva consulta futura`() = runTest {
        // Tras cerrar sesión se eliminan los tokens y una nueva consulta debe volver a pedir getMe().
        coEvery { apiService.login(any()) } returns AuthResponse(
            token = "jwt-token",
            refreshToken = "refresh-token",
            email = "ana@example.com",
            userId = "1",
            roles = emptyList()
        )
        coEvery { apiService.getMe() } returns sampleUserResponse()
        every { tokenManager.clear() } returns Unit
        repository.login("ana@example.com", "secreto")

        repository.logout()

        verify { tokenManager.clear() }
        coEvery { apiService.getMe() } returns sampleUserResponse(id = "99", firstName = "Nueva", lastName = "Sesion")
        val refreshed = repository.getCurrentUser()
        // Debe haberse realizado una segunda llamada porque la cache quedó vacía.
        coVerify(exactly = 2) { apiService.getMe() }
        assertEquals("Nueva Sesion", refreshed?.name)
    }
}
