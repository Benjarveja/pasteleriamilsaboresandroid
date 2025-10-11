package com.example.pasteleriamilssaboresandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pasteleriamilssaboresandroid.ui.products.ProductsScreen

sealed class Screen(val route: String) {
    data object Products : Screen("products")
    // data object Cart : Screen("cart")
    // data object Checkout : Screen("checkout")
    // data object News : Screen("news")
}

@Composable
fun AppNavHost(startDestination: String = Screen.Products.route) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Products.route) { ProductsScreen() }
        // composable(Screen.Cart.route) { CartScreen() }
        // composable(Screen.Checkout.route) { CheckoutScreen() }
        // composable(Screen.News.route) { NewsScreen() }
    }
}

