package com.example.pasteleriamilssaboresandroid.data

data class ImageSliderItem(val image: String, val title: String, val subtitle: String)

val imageSliderItems = listOf(
    // Ajuste: asegurarse de usar únicamente imágenes que existen en assets. En este proyecto
    // hay dos imágenes presentes: noticiaguinness.jpg y noticiadiaadia.jpg dentro de images/.
    ImageSliderItem("images/noticiaguinness.jpg", "50 Años de Dulce Tradición", "Acompañándote en tus celebraciones más importantes."),
    ImageSliderItem("images/noticiadiaadia.jpg", "Nuevos Sabores Cada Día", "Descubre nuestras últimas creaciones y déjate sorprender."),
    ImageSliderItem("images/noticiaguinness.jpg", "Una Historia de Récord", "Orgullosos de nuestra participación en el Récord Guinness de 1995.")
)
