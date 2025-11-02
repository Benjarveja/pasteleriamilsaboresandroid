package com.example.pasteleriamilssaboresandroid.ui.theme.screen.user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pasteleriamilssaboresandroid.data.database.order.Order
import com.example.pasteleriamilssaboresandroid.util.formatCLP
import com.example.pasteleriamilssaboresandroid.util.getOrderStatus
import com.example.pasteleriamilssaboresandroid.viewmodel.OrdersViewModel
import com.example.pasteleriamilssaboresandroid.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OrdersScreen(ordersViewModel: OrdersViewModel, userViewModel: UserViewModel) {
    val uiState by ordersViewModel.uiState.collectAsStateWithLifecycle()
    val loggedInUser by userViewModel.loggedInUser.collectAsStateWithLifecycle()

    LaunchedEffect(loggedInUser) {
        loggedInUser?.id?.let {
            ordersViewModel.loadOrders(it)
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            Text(text = "Mis Pedidos", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.orders.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Aún no tienes pedidos.")
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.orders) { order ->
                            OrderCard(order = order)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    var expanded by remember { mutableStateOf(false) }
    val status = getOrderStatus(order)
    val formattedDate = remember(order.createdAt) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(order.createdAt)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Pedido #${order.code}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = formattedDate, style = MaterialTheme.typography.bodySmall)
            }
            Text(text = "Estado: ${status.displayName}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Productos:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    order.items.forEach { item ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = "${item.quantity}x ${item.name}")
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = formatCLP(item.lineTotal))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Información de Entrega:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Opción: ${if (order.deliveryOption == "delivery") "Despacho a domicilio" else "Retiro en tienda"}")
                    if (order.deliveryOption == "delivery") {
                        Text("Dirección: ${order.address}")
                    } else {
                        Text("Sucursal: ${order.branch}")
                        Text("Horario: ${order.pickupTimeSlot}")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total", fontWeight = FontWeight.Bold)
                        Text(text = formatCLP(order.total), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
