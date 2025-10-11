package com.example.pasteleriamilssaboresandroid.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pasteleriamilssaboresandroid.ui.cart.CartViewModel
import com.example.pasteleriamilssaboresandroid.ui.news.NewsScreen
import com.example.pasteleriamilssaboresandroid.ui.products.ProductsScreen
import com.example.pasteleriamilssaboresandroid.ui.cart.CartScreen
import com.example.pasteleriamilssaboresandroid.ui.checkout.CheckoutScreen

sealed class Screen(val route: String) {
    data object Products : Screen("products")
    data object News : Screen("news")
    data object Cart : Screen("cart")
    data object Checkout : Screen("checkout")
    data object ProductDetail : Screen("product/{id}") { const val ID = "id" }
}

data class BottomItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun AppNavHost(startDestination: String = Screen.Products.route) {
    val navController = rememberNavController()
    val cartVM: CartViewModel = viewModel()

    val items = listOf(
        BottomItem(Screen.Products.route, "Productos", Icons.AutoMirrored.Filled.List),
        BottomItem(Screen.News.route, "Noticias", Icons.Filled.Info),
        BottomItem(Screen.Cart.route, "Carrito", Icons.Filled.ShoppingCart),
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { item ->
                    val selected = currentDestination.isRouteInHierarchy(item.route)
                    NavigationBarItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = selected,
                        onClick = {
                            if (!selected) navController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        androidx.compose.foundation.layout.Box(modifier = androidx.compose.ui.Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = startDestination) {
                composable(Screen.Products.route) {
                    ProductsScreen(
                        cartVM = cartVM,
                        onOpen = { id -> navController.navigate("product/$id") }
                    )
                }
                composable(Screen.News.route) { NewsScreen() }
                composable(Screen.Cart.route) {
                    CartScreen(
                        cartVM = cartVM,
                        onCheckout = { navController.navigate(Screen.Checkout.route) }
                    )
                }
                composable(Screen.Checkout.route) {
                    CheckoutScreen(
                        cartVM = cartVM,
                        onFinish = {
                            navController.navigate(Screen.Products.route) {
                                launchSingleTop = true
                                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                            }
                        }
                    )
                }
                composable(Screen.ProductDetail.route) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString(Screen.ProductDetail.ID) ?: ""
                    com.example.pasteleriamilssaboresandroid.ui.products.ProductDetailScreen(
                        productId = id,
                        cartVM = cartVM,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: String): Boolean {
    return this?.hierarchy?.any { it.route == route } == true
}
