package com.example.pasteleriamilssaboresandroid.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pasteleriamilssaboresandroid.PasteleriaApp
import com.example.pasteleriamilssaboresandroid.ui.cart.CartScreen
import com.example.pasteleriamilssaboresandroid.ui.cart.CartViewModel
import com.example.pasteleriamilssaboresandroid.ui.cart.CartViewModelFactory
import com.example.pasteleriamilssaboresandroid.ui.checkout.CheckoutScreen
import com.example.pasteleriamilssaboresandroid.ui.news.NewsScreen
import com.example.pasteleriamilssaboresandroid.ui.products.ProductDetailScreen
import com.example.pasteleriamilssaboresandroid.ui.products.ProductsScreen
import com.example.pasteleriamilssaboresandroid.ui.theme.screen.home.HomeScreen
import com.example.pasteleriamilssaboresandroid.ui.theme.screen.user.LoginScreen
import com.example.pasteleriamilssaboresandroid.ui.theme.screen.user.OrdersScreen
import com.example.pasteleriamilssaboresandroid.ui.theme.screen.user.ProfileScreen
import com.example.pasteleriamilssaboresandroid.ui.theme.screen.user.RegisterScreen
import com.example.pasteleriamilssaboresandroid.viewmodel.LoginResult
import com.example.pasteleriamilssaboresandroid.viewmodel.RegisterResult
import com.example.pasteleriamilssaboresandroid.viewmodel.UserViewModel
import com.example.pasteleriamilssaboresandroid.viewmodel.UserViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(startDestination: String = Screen.Home.route) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as PasteleriaApp

    val cartVM: CartViewModel = viewModel(factory = CartViewModelFactory(application.cartRepository))
    val userVM: UserViewModel = viewModel(factory = UserViewModelFactory(application.userRepository))
    val cartUi by cartVM.ui.collectAsStateWithLifecycle()
    val loginResult by userVM.loginResult.collectAsStateWithLifecycle()
    val registerResult by userVM.registerResult.collectAsStateWithLifecycle()
    val loggedInUser by userVM.loggedInUser.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showMenu by remember { mutableStateOf(false) }

    val items = listOf(
        BottomItem(Screen.Home.route, "Inicio", Icons.Filled.Home),
        BottomItem(Screen.Products.route, "Productos", Icons.AutoMirrored.Filled.List),
        BottomItem(Screen.News.route, "Noticias", Icons.Filled.Info),
        BottomItem(Screen.Cart.route, "Carrito", Icons.Filled.ShoppingCart),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.route !in listOf(Screen.LoginFlow.route, Screen.Register.route)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (showBottomBar) {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Pastelería MilSabores") },
                    actions = {
                        if (loggedInUser == null) {
                            IconButton(onClick = { navController.navigate(Screen.LoginFlow.route) }) {
                                Icon(Icons.Filled.Person, contentDescription = "Login")
                            }
                        } else {
                            Box {
                                IconButton(onClick = { showMenu = !showMenu }) {
                                    Icon(Icons.Filled.Person, contentDescription = "User Menu")
                                }
                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Administrar Perfil") },
                                        onClick = {
                                            navController.navigate(Screen.Profile.route)
                                            showMenu = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Ver Pedidos") },
                                        onClick = {
                                            navController.navigate(Screen.Orders.route)
                                            showMenu = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Cerrar Sesión") },
                                        onClick = {
                                            userVM.logout()
                                            showMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    items.forEach { item ->
                        val selected = currentDestination.isRouteInHierarchy(item.route)
                        NavigationBarItem(
                            icon = {
                                if (item.route == Screen.Cart.route) {
                                    BadgedBox(
                                        badge = {
                                            if (cartUi.itemCount > 0) {
                                                Badge { Text("${cartUi.itemCount}") }
                                            }
                                        }
                                    ) {
                                        Icon(imageVector = item.icon, contentDescription = item.label)
                                    }
                                } else {
                                    Icon(imageVector = item.icon, contentDescription = item.label)
                                }
                            },
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
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = startDestination) {
                composable(Screen.Home.route) {
                    HomeScreen(onLoginClick = { navController.navigate(Screen.LoginFlow.route) })
                }
                composable(Screen.LoginFlow.route) {
                    LoginScreen(
                        onLoginClick = { email, password ->
                            userVM.login(email, password)
                        },
                        onContinueAsGuestClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.LoginFlow.route) { inclusive = true }
                            }
                        },
                        onRegisterClick = {
                            navController.navigate(Screen.Register.route)
                        }
                    )
                }
                composable(Screen.Register.route) {
                    RegisterScreen(
                        onRegisterClick = { firstName, lastName, email, password, phone, run, birthDate, region, comuna, street ->
                            userVM.registerUser(
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                password = password,
                                phone = phone,
                                run = run,
                                birthDate = birthDate,
                                region = region,
                                comuna = comuna,
                                street = street
                            )
                        }
                    )
                }
                composable(Screen.Products.route) {
                    ProductsScreen(
                        cartVM = cartVM,
                        onOpen = { id -> navController.navigate("product/$id") },
                        onShowSnackbar = { message ->
                            scope.launch {
                                snackbarHostState.showSnackbar(message)
                            }
                        }
                    )
                }
                composable(Screen.News.route) { NewsScreen() }
                composable(Screen.Cart.route) {
                    CartScreen(
                        cartVM = cartVM,
                        onCheckout = {
                            navController.navigate(Screen.Checkout.route)
                        }
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
                    ProductDetailScreen(
                        productId = id,
                        cartVM = cartVM,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(userViewModel = userVM)
                }
                composable(Screen.Orders.route) {
                    OrdersScreen()
                }
            }
        }
    }

    LaunchedEffect(loginResult) {
        when (val result = loginResult) {
            is LoginResult.Success -> {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.LoginFlow.route) { inclusive = true }
                }
                userVM.resetLoginState()
            }
            is LoginResult.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(result.message)
                }
                userVM.resetLoginState()
            }
            else -> {}
        }
    }

    LaunchedEffect(registerResult) {
        when (val result = registerResult) {
            is RegisterResult.Success -> {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.LoginFlow.route) { inclusive = true }
                }
                userVM.resetRegisterState()
            }
            is RegisterResult.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(result.message)
                }
                userVM.resetRegisterState()
            }
            else -> {}
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: String): Boolean {
    return this?.hierarchy?.any { it.route == route } == true
}
