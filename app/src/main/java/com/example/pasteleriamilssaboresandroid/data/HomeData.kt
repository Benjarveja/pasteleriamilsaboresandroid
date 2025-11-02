package com.example.pasteleriamilssaboresandroid.data

data class ImageSliderItem(val image: String, val title: String, val subtitle: String)

val imageSliderItems = listOf(
    ImageSliderItem("images/products/TC001.jpg", "Torta de Chocolate", "La mejor torta de chocolate del mundo"),
    ImageSliderItem("images/products/PT001.jpg", "Pie de Limón", "El equilibrio perfecto entre dulce y ácido"),
    ImageSliderItem("images/products/PV001.jpg", "Torta de Vainilla", "Un clásico que nunca falla"),
)
