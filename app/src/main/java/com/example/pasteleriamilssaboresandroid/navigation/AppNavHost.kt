package com.example.pasteleriamilssaboresandroid.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import com.example.pasteleriamilssaboresandroid.ui.news.NewsScreen
import com.example.pasteleriamilssaboresandroid.ui.products.ProductsScreen
import androidx.compose.material3.Scaffold

sealed class Screen(val route: String) {
    data object Products : Screen("products")
    data object News : Screen("news")
}

data class BottomItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun AppNavHost(startDestination: String = Screen.Products.route) {
    val navController = rememberNavController()

    val items = listOf(
        BottomItem(Screen.Products.route, "Productos", Icons.Filled.List),
        BottomItem(Screen.News.route, "Noticias", Icons.Filled.Info),
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
                composable(Screen.Products.route) { ProductsScreen() }
                composable(Screen.News.route) { NewsScreen() }
            }
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: String): Boolean {
    return this?.hierarchy?.any { it.route == route } == true
}
