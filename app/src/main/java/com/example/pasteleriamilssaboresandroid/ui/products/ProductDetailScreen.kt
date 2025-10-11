package com.example.pasteleriamilssaboresandroid.ui.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.pasteleriamilssaboresandroid.data.repository.AssetsProductRepository
import com.example.pasteleriamilssaboresandroid.ui.cart.CartViewModel
import com.example.pasteleriamilssaboresandroid.util.formatCLP

@Composable
fun ProductDetailScreen(
    productId: String,
    cartVM: CartViewModel,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val vm: ProductDetailViewModel = viewModel(
        factory = ProductDetailViewModelFactory(AssetsProductRepository(context.assets), productId)
    )
    val ui by vm.ui.collectAsStateWithLifecycle()

    when {
        ui.isLoading -> Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CircularProgressIndicator()
        }
        ui.error != null -> Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("Ocurrió un error: ${ui.error}")
            Spacer(Modifier.size(8.dp))
            OutlinedButton(onClick = { vm.load() }) { Text("Reintentar") }
            Spacer(Modifier.size(8.dp))
            OutlinedButton(onClick = onBack) { Text("Volver") }
        }
        else -> {
            val p = ui.product ?: return
            val assetPath = p.image?.let { "file:///android_asset/$it" }
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context).data(assetPath).crossfade(true).build()
            )
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                OutlinedButton(onClick = onBack) { Text("Volver") }
                Spacer(Modifier.size(12.dp))
                if (assetPath != null) {
                    Image(
                        painter = painter,
                        contentDescription = p.name,
                        modifier = Modifier.fillMaxWidth().height(220.dp).clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(Modifier.size(12.dp))
                Text(p.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(p.category, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.size(8.dp))
                p.description?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
                Spacer(Modifier.size(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(formatCLP(p.price), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Button(onClick = { cartVM.add(p) }) { Text("Agregar al carrito") }
                }
            }
        }
    }
}

