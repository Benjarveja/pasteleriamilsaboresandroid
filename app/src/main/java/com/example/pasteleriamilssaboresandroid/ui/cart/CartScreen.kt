package com.example.pasteleriamilssaboresandroid.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.pasteleriamilssaboresandroid.util.formatCLP

@Composable
fun CartScreen(cartVM: CartViewModel, onCheckout: () -> Unit) {
    val ui by cartVM.ui.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Carrito", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        if (ui.items.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío")
            }
            return
        }

        LazyColumn(
            modifier = Modifier.weight(1f).padding(top = 12.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(ui.items, key = { it.productId }) { item ->
                CartItemRow(
                    name = item.name,
                    price = item.price,
                    image = item.image,
                    qty = item.quantity,
                    onInc = { cartVM.increment(item.productId) },
                    onDec = { cartVM.decrement(item.productId) },
                    onRemove = { cartVM.remove(item.productId) }
                )
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Subtotal", style = MaterialTheme.typography.titleMedium)
            Text(formatCLP(ui.subtotal), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
            Text("Ir al checkout")
        }
    }
}

@Composable
private fun CartItemRow(
    name: String,
    price: Int,
    image: String?,
    qty: Int,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onRemove: () -> Unit,
) {
    val context = LocalContext.current
    val assetPath = image?.let { "file:///android_asset/$it" }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context).data(assetPath).crossfade(true).build()
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            if (assetPath != null) {
                Image(painter = painter, contentDescription = name, modifier = Modifier.size(56.dp).clip(MaterialTheme.shapes.small))
                Spacer(Modifier.size(12.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(formatCLP(price), style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDec) { Icon(Icons.Filled.Remove, contentDescription = "Disminuir") }
                Text("$qty", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = onInc) { Icon(Icons.Filled.Add, contentDescription = "Aumentar") }
                Spacer(Modifier.size(8.dp))
                IconButton(onClick = onRemove) { Icon(Icons.Filled.Delete, contentDescription = "Eliminar") }
            }
        }
    }
}
