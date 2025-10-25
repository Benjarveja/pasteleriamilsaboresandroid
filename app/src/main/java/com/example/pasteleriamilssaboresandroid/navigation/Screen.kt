package com.example.pasteleriamilssaboresandroid.navigation

import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Products : Screen("products")
    object LoginFlow : Screen("login_flow")
    object Register : Screen("register")
    object News : Screen("news")
    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object Profile : Screen("profile")
    object Orders : Screen("orders")
    object ProductDetail : Screen("product/{id}") {
        const val ID = "id"
    }
}


data class BottomItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

