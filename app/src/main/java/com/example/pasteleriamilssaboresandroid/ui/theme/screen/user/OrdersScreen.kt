package com.example.pasteleriamilssaboresandroid.ui.theme.screen.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OrdersScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Mis Pedidos", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        // Aquí se mostrará la lista de pedidos del usuario.
    }
}

