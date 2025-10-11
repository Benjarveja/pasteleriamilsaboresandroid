package com.example.pasteleriamilssaboresandroid.ui.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.pasteleriamilssaboresandroid.data.repository.AssetsProductRepository
import com.example.pasteleriamilssaboresandroid.domain.model.Product
import com.example.pasteleriamilssaboresandroid.util.formatCLP

@Composable
fun ProductsScreen() {
    val context = LocalContext.current
    val vm: ProductsViewModel = viewModel(
        factory = ProductsViewModelFactory(AssetsProductRepository(context.assets))
    )
    val ui by vm.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Productos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = ui.query,
            onValueChange = vm::onQueryChange,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            label = { Text("Buscar productos") }
        )

        when {
            ui.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            ui.error != null -> Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Ocurrió un error: ${ui.error}")
                Button(onClick = { vm.load() }, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Reintentar")
                }
            }
            else -> ProductList(products = vm.filtered(ui))
        }
    }
}

@Composable
private fun ProductList(products: List<Product>) {
    if (products.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay productos que coincidan")
        }
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top = 12.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products, key = { it.id }) { p -> ProductItem(p) }
    }
}

@Composable
private fun ProductItem(p: Product) {
    val context = LocalContext.current
    val assetPath = p.image?.let { "file:///android_asset/$it" }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(assetPath)
            .crossfade(true)
            .build()
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            if (assetPath != null) {
                Image(
                    painter = painter,
                    contentDescription = p.name,
                    modifier = Modifier.size(64.dp).clip(MaterialTheme.shapes.small)
                )
                Spacer(Modifier.size(12.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(text = p.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(text = p.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            }
            Text(text = formatCLP(p.price), style = MaterialTheme.typography.titleMedium)
        }
    }
}
