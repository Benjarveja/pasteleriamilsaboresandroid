package com.example.pasteleriamilssaboresandroid.ui.checkout

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pasteleriamilssaboresandroid.data.ChileanGeographicData
import com.example.pasteleriamilssaboresandroid.data.PaymentMethod
import com.example.pasteleriamilssaboresandroid.data.paymentMethods
import com.example.pasteleriamilssaboresandroid.data.pickupBranches
import com.example.pasteleriamilssaboresandroid.data.pickupTimeSlots
import com.example.pasteleriamilssaboresandroid.ui.cart.CartViewModel
import com.example.pasteleriamilssaboresandroid.util.formatCLP
import com.example.pasteleriamilssaboresandroid.viewmodel.UserViewModel
import java.util.Calendar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartVM: CartViewModel,
    userVM: UserViewModel,
    checkoutVM: CheckoutViewModel,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    val cartUi by cartVM.ui.collectAsStateWithLifecycle()
    val loggedInUser by userVM.loggedInUser.collectAsStateWithLifecycle()
    val checkoutUi by checkoutVM.uiState.collectAsStateWithLifecycle()
    val formData by checkoutVM.formData.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var deliveryExpanded by remember { mutableStateOf(false) }
    var regionExpanded by remember { mutableStateOf(false) }
    var comunaExpanded by remember { mutableStateOf(false) }
    var branchExpanded by remember { mutableStateOf(false) }
    var timeSlotExpanded by remember { mutableStateOf(false) }
    var paymentExpanded by remember { mutableStateOf(false) }

    val selectedPayment = paymentMethods.find { it.id == formData.paymentMethod } ?: paymentMethods.first()

    LaunchedEffect(loggedInUser, cartUi.subtotal) {
        checkoutVM.loadForm(cartUi.subtotal)
    }
    LaunchedEffect(checkoutUi.error) {
        checkoutUi.error?.let { message ->
            coroutineScope.launch { snackbarHostState.showSnackbar(message) }
        }
    }
    LaunchedEffect(checkoutUi.order) {
        if (checkoutUi.order != null && checkoutUi.error == null && !checkoutUi.isLoading) {
            onFinish()
        }
    }

    val deliveryOptions = mapOf("delivery" to "Despacho a Domicilio", "pickup" to "Retiro en Tienda")
    val regions = ChileanGeographicData.regionsAndCommunes.keys.toList()
    val communes by remember(formData.region) {
        mutableStateOf(ChileanGeographicData.regionsAndCommunes[formData.region] ?: emptyList())
    }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            checkoutVM.updateFormData(formData.copy(birthDate = "$year-${month + 1}-$day"))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Finalizar Compra") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Personal Data
            Text("Datos de Contacto", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(vertical = 8.dp))
            OutlinedTextField(value = formData.run, onValueChange = { checkoutVM.updateFormData(formData.copy(run = it)) }, label = { Text("RUN") }, modifier = Modifier.fillMaxWidth(), isError = checkoutUi.validationErrors.containsKey("run"), supportingText = { checkoutUi.validationErrors["run"]?.let { Text(it) } })
            OutlinedTextField(value = formData.firstName, onValueChange = { checkoutVM.updateFormData(formData.copy(firstName = it)) }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), isError = checkoutUi.validationErrors.containsKey("firstName"), supportingText = { checkoutUi.validationErrors["firstName"]?.let { Text(it) } })
            OutlinedTextField(value = formData.lastName, onValueChange = { checkoutVM.updateFormData(formData.copy(lastName = it)) }, label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth(), isError = checkoutUi.validationErrors.containsKey("lastName"), supportingText = { checkoutUi.validationErrors["lastName"]?.let { Text(it) } })
            OutlinedTextField(value = formData.email, onValueChange = { checkoutVM.updateFormData(formData.copy(email = it)) }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), isError = checkoutUi.validationErrors.containsKey("email"), supportingText = { checkoutUi.validationErrors["email"]?.let { Text(it) } })
            OutlinedTextField(value = formData.phone, onValueChange = { checkoutVM.updateFormData(formData.copy(phone = it)) }, label = { Text("Celular") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth(), isError = checkoutUi.validationErrors.containsKey("phone"), supportingText = { checkoutUi.validationErrors["phone"]?.let { Text(it) } })
            Button(onClick = { datePickerDialog.show() }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Text(if (formData.birthDate.isEmpty()) "Seleccionar Fecha de Nacimiento" else formData.birthDate)
            }
            if (checkoutUi.validationErrors.containsKey("birthDate")) {
                Text(checkoutUi.validationErrors["birthDate"]!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // Delivery Options
            Text("Opciones de Entrega", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(vertical = 8.dp))
            ExposedDropdownMenuBox(expanded = deliveryExpanded, onExpandedChange = { deliveryExpanded = !deliveryExpanded }) {
                OutlinedTextField(value = deliveryOptions[formData.deliveryOption] ?: "", onValueChange = {}, readOnly = true, label = { Text("Opción de Entrega") }, modifier = Modifier.menuAnchor().fillMaxWidth(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = deliveryExpanded) })
                ExposedDropdownMenu(expanded = deliveryExpanded, onDismissRequest = { deliveryExpanded = false }) {
                    deliveryOptions.forEach { (key, value) ->
                        DropdownMenuItem(text = { Text(value) }, onClick = {
                            checkoutVM.updateFormData(formData.copy(deliveryOption = key))
                            deliveryExpanded = false
                        })
                    }
                }
            }

            if (formData.deliveryOption == "delivery") {
                OutlinedTextField(value = formData.street, onValueChange = { checkoutVM.updateFormData(formData.copy(street = it)) }, label = { Text("Calle y Número") }, modifier = Modifier.fillMaxWidth(), isError = checkoutUi.validationErrors.containsKey("street"), supportingText = { checkoutUi.validationErrors["street"]?.let { Text(it) } })
                ExposedDropdownMenuBox(expanded = regionExpanded, onExpandedChange = { regionExpanded = !regionExpanded }) {
                    OutlinedTextField(value = formData.region, onValueChange = {}, readOnly = true, label = { Text("Región") }, modifier = Modifier.menuAnchor().fillMaxWidth(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) }, isError = checkoutUi.validationErrors.containsKey("region"))
                    ExposedDropdownMenu(expanded = regionExpanded, onDismissRequest = { regionExpanded = false }) {
                        regions.forEach { region ->
                            DropdownMenuItem(text = { Text(region) }, onClick = {
                                checkoutVM.updateFormData(formData.copy(region = region, comuna = ""))
                                regionExpanded = false
                            })
                        }
                    }
                }
                ExposedDropdownMenuBox(expanded = comunaExpanded, onExpandedChange = { comunaExpanded = !comunaExpanded }) {
                    OutlinedTextField(value = formData.comuna, onValueChange = {}, readOnly = true, label = { Text("Comuna") }, modifier = Modifier.menuAnchor().fillMaxWidth(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = comunaExpanded) }, enabled = communes.isNotEmpty(), isError = checkoutUi.validationErrors.containsKey("comuna"))
                    ExposedDropdownMenu(expanded = comunaExpanded, onDismissRequest = { comunaExpanded = false }) {
                        communes.forEach { comuna ->
                            DropdownMenuItem(text = { Text(comuna) }, onClick = {
                                checkoutVM.updateFormData(formData.copy(comuna = comuna))
                                comunaExpanded = false
                            })
                        }
                    }
                }
            } else {
                ExposedDropdownMenuBox(expanded = branchExpanded, onExpandedChange = { branchExpanded = !branchExpanded }) {
                    OutlinedTextField(value = pickupBranches.find { it.id == formData.branch }?.name ?: "", onValueChange = {}, readOnly = true, label = { Text("Sucursal") }, modifier = Modifier.menuAnchor().fillMaxWidth(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = branchExpanded) }, isError = checkoutUi.validationErrors.containsKey("branch"))
                    ExposedDropdownMenu(expanded = branchExpanded, onDismissRequest = { branchExpanded = false }) {
                        pickupBranches.forEach { branch ->
                            DropdownMenuItem(text = { Text(branch.name) }, onClick = {
                                checkoutVM.updateFormData(formData.copy(branch = branch.id))
                                branchExpanded = false
                            })
                        }
                    }
                }
                ExposedDropdownMenuBox(expanded = timeSlotExpanded, onExpandedChange = { timeSlotExpanded = !timeSlotExpanded }) {
                    OutlinedTextField(value = pickupTimeSlots.find { it.id == formData.pickupTimeSlot }?.label ?: "", onValueChange = {}, readOnly = true, label = { Text("Horario de Retiro") }, modifier = Modifier.menuAnchor().fillMaxWidth(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = timeSlotExpanded) }, isError = checkoutUi.validationErrors.containsKey("pickupTimeSlot"))
                    ExposedDropdownMenu(expanded = timeSlotExpanded, onDismissRequest = { timeSlotExpanded = false }) {
                        pickupTimeSlots.forEach { slot ->
                            DropdownMenuItem(text = { Text(slot.label) }, onClick = {
                                checkoutVM.updateFormData(formData.copy(pickupTimeSlot = slot.id))
                                timeSlotExpanded = false
                            })
                        }
                    }
                }
            }
            
            // Coupon
            Text("Cupón de Descuento", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(vertical = 8.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(value = formData.couponCode, onValueChange = { checkoutVM.updateFormData(formData.copy(couponCode = it)) }, label = { Text("Código") }, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { checkoutVM.applyCoupon(cartUi.subtotal) }) {
                    Text("Aplicar")
                }
            }

            Text("Método de pago", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(vertical = 8.dp))
            ExposedDropdownMenuBox(expanded = paymentExpanded, onExpandedChange = { paymentExpanded = !paymentExpanded }) {
                OutlinedTextField(
                    value = selectedPayment.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecciona un método") },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = paymentExpanded) },
                    isError = checkoutUi.validationErrors.containsKey("paymentMethod")
                )
                ExposedDropdownMenu(expanded = paymentExpanded, onDismissRequest = { paymentExpanded = false }) {
                    paymentMethods.forEach { method ->
                        DropdownMenuItem(text = { Text(method.label) }, onClick = {
                            checkoutVM.updateFormData(formData.copy(paymentMethod = method.id))
                            paymentExpanded = false
                        })
                    }
                }
            }

            // Summary
            Text("Resumen", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(vertical = 8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal")
                Text(formatCLP(checkoutUi.pricing.subtotal))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Descuento")
                Text(formatCLP(checkoutUi.pricing.couponDiscount))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", style = MaterialTheme.typography.titleLarge)
                Text(formatCLP(checkoutUi.pricing.total), style = MaterialTheme.typography.titleLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { checkoutVM.processOrder(cartUi.items) },
                modifier = Modifier.fillMaxWidth(),
                enabled = checkoutUi.isFormValid && !checkoutUi.isLoading
            ) {
                Text(if (checkoutUi.isLoading) "Procesando..." else "Confirmar Pedido")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}