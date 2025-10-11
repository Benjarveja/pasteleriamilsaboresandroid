package com.example.pasteleriamilssaboresandroid.ui.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pasteleriamilssaboresandroid.ui.cart.CartViewModel
import com.example.pasteleriamilssaboresandroid.util.*

@Composable
fun CheckoutScreen(cartVM: CartViewModel, onFinish: () -> Unit) {
    val ui by cartVM.ui.collectAsStateWithLifecycle()

    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var run by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var region by rememberSaveable { mutableStateOf("") }
    var comuna by rememberSaveable { mutableStateOf("") }
    var street by rememberSaveable { mutableStateOf("") }
    var deliveryMethod by rememberSaveable { mutableStateOf("delivery") }
    var paymentMethod by rememberSaveable { mutableStateOf("card") }

    var errors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    fun validate(): Boolean {
        val newErrors = mutableMapOf<String, String>()
        if (!hasMinLength(firstName, 2)) newErrors["firstName"] = "Nombre requerido (mín. 2 caracteres)"
        if (!hasMinLength(lastName, 2)) newErrors["lastName"] = "Apellido requerido (mín. 2 caracteres)"
        if (!isValidRun(run)) newErrors["run"] = "RUN inválido"
        if (!isValidEmail(email)) newErrors["email"] = "Email inválido"
        if (!isValidChileanPhone(phone)) newErrors["phone"] = "Teléfono inválido (debe comenzar con 9 y tener 9 dígitos)"
        if (!hasMinLength(street, 5)) newErrors["street"] = "Dirección requerida (mín. 5 caracteres)"
        if (!hasMinLength(region, 3)) newErrors["region"] = "Región requerida"
        if (!hasMinLength(comuna, 3)) newErrors["comuna"] = "Comuna requerida"
        errors = newErrors
        return newErrors.isEmpty()
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Checkout", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.size(12.dp))
        Text("Total a pagar: ${formatCLP(ui.total)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.size(16.dp))

        OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(),
            isError = errors.containsKey("firstName"), supportingText = { errors["firstName"]?.let { Text(it) } })
        Spacer(Modifier.size(8.dp))
        OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth(),
            isError = errors.containsKey("lastName"), supportingText = { errors["lastName"]?.let { Text(it) } })
        Spacer(Modifier.size(8.dp))
        OutlinedTextField(value = run, onValueChange = { run = it }, label = { Text("RUN (ej: 12.345.678-5)") }, modifier = Modifier.fillMaxWidth(),
            isError = errors.containsKey("run"), supportingText = { errors["run"]?.let { Text(it) } })
        Spacer(Modifier.size(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(),
            isError = errors.containsKey("email"), supportingText = { errors["email"]?.let { Text(it) } })
        Spacer(Modifier.size(8.dp))
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono (ej: 987654321)") }, modifier = Modifier.fillMaxWidth(),
            isError = errors.containsKey("phone"), supportingText = { errors["phone"]?.let { Text(it) } })
        Spacer(Modifier.size(8.dp))
        OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth(),
            isError = errors.containsKey("street"), supportingText = { errors["street"]?.let { Text(it) } })
        Spacer(Modifier.size(8.dp))
        OutlinedTextField(value = comuna, onValueChange = { comuna = it }, label = { Text("Comuna") }, modifier = Modifier.fillMaxWidth(),
            isError = errors.containsKey("comuna"), supportingText = { errors["comuna"]?.let { Text(it) } })
        Spacer(Modifier.size(8.dp))
        OutlinedTextField(value = region, onValueChange = { region = it }, label = { Text("Región") }, modifier = Modifier.fillMaxWidth(),
            isError = errors.containsKey("region"), supportingText = { errors["region"]?.let { Text(it) } })

        Spacer(Modifier.size(16.dp))
        Text("Método de entrega", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.size(8.dp))
        Row(Modifier.fillMaxWidth()) {
            FilterChip(selected = deliveryMethod == "delivery", onClick = { deliveryMethod = "delivery" }, label = { Text("Envío a domicilio") }, modifier = Modifier.weight(1f))
            Spacer(Modifier.size(8.dp))
            FilterChip(selected = deliveryMethod == "pickup", onClick = { deliveryMethod = "pickup" }, label = { Text("Retiro en tienda") }, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.size(16.dp))
        Text("Método de pago", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.size(8.dp))
        Row(Modifier.fillMaxWidth()) {
            FilterChip(selected = paymentMethod == "card", onClick = { paymentMethod = "card" }, label = { Text("Tarjeta") }, modifier = Modifier.weight(1f))
            Spacer(Modifier.size(8.dp))
            FilterChip(selected = paymentMethod == "transfer", onClick = { paymentMethod = "transfer" }, label = { Text("Transferencia") }, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.size(16.dp))
        Button(
            enabled = ui.items.isNotEmpty(),
            onClick = {
                if (validate()) {
                    cartVM.clear()
                    onFinish()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Confirmar pedido") }
    }
}
