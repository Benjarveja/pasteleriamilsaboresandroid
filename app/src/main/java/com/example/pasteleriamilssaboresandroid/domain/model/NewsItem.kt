package com.example.pasteleriamilssaboresandroid.domain.model

data class NewsItem(
    val id: String,
    val title: String,
    val excerpt: String,
    val image: String? = null,
    val href: String? = null,
)
