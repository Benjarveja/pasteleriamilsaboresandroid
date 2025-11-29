package com.example.pasteleriamilssaboresandroid.data.network

import com.example.pasteleriamilssaboresandroid.data.network.dto.ProductResponse
import com.example.pasteleriamilssaboresandroid.domain.model.Product

fun ProductResponse.toDomain(): Product {
    return Product(
        productId = this.codigo,
        productName = this.nombre,
        price = this.precio,
        category = this.categoria,
        productImage = this.imagenUrl,
        productDescription = this.descripcion,
        popular = this.popular,
        history = this.historia
    )
}

