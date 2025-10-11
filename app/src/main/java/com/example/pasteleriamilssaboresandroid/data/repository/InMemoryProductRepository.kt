package com.example.pasteleriamilssaboresandroid.data.repository

import com.example.pasteleriamilssaboresandroid.domain.model.Product
import kotlinx.coroutines.delay

class InMemoryProductRepository : ProductRepository {
    override suspend fun getProducts(): List<Product> {
        // Simula latencia
        delay(300)
        return listOf(
            Product(id = "TC001", name = "Torta de Chocolate", price = 15900, category = "Tortas"),
            Product(id = "TT002", name = "Tarta de Frutillas", price = 13900, category = "Tartas"),
            Product(id = "PG001", name = "Pie de Limón", price = 9900, category = "Pies"),
            Product(id = "PV002", name = "Pastel de Vainilla", price = 14900, category = "Tortas"),
            Product(id = "TE001", name = "Tres Leches", price = 16900, category = "Tortas"),
        )
    }
}
