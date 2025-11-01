package com.example.pasteleriamilssaboresandroid.ui.theme.screen.user

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.pasteleriamilssaboresandroid.data.ChileanGeographicData
import com.example.pasteleriamilssaboresandroid.util.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String, String, String, String, String, String, String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var run by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }

    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var runError by remember { mutableStateOf<String?>(null) }
    var birthDateError by remember { mutableStateOf<String?>(null) }
    var regionError by remember { mutableStateOf<String?>(null) }
    var comunaError by remember { mutableStateOf<String?>(null) }
    var streetError by remember { mutableStateOf<String?>(null) }

    val isFormValid by derivedStateOf {
        isNonEmpty(firstName) && isNonEmpty(lastName) &&
                isValidEmail(email) && hasMinLength(password, 8) &&
                isValidChileanPhone(phone) && isValidRun(run) &&
                isValidBirthDate(birthDate) && isNonEmpty(region) &&
                isNonEmpty(comuna) && isNonEmpty(street)
    }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            birthDate = "$selectedYear-${selectedMonth + 1}-${selectedDayOfMonth}"
            birthDateError = if (!isValidBirthDate(birthDate)) "Fecha de nacimiento no válida" else null
        }, year, month, day
    )

    val regions = ChileanGeographicData.regionsAndCommunes.keys.toList()
    var communes by remember { mutableStateOf<List<String>>(emptyList()) }
    var regionExpanded by remember { mutableStateOf(false) }
    var communeExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Usuario") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = {
                    firstName = it
                    firstNameError = if (!isNonEmpty(it)) "El nombre es requerido" else null
                },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                isError = firstNameError != null,
                supportingText = { firstNameError?.let { Text(it) } }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                    lastNameError = if (!isNonEmpty(it)) "El apellido es requerido" else null
                },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth(),
                isError = lastNameError != null,
                supportingText = { lastNameError?.let { Text(it) } }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = if (!isValidEmail(it)) "Email no válido" else null
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it) } }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = if (!hasMinLength(it, 8)) "La contraseña debe tener al menos 8 caracteres" else null
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError != null,
                supportingText = { passwordError?.let { Text(it) } }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    phoneError = if (!isValidChileanPhone(it)) "Teléfono no válido (ej: 912345678)" else null
                },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = phoneError != null,
                supportingText = { phoneError?.let { Text(it) } }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = run,
                onValueChange = {
                    val filtered = it.filter { char -> char.isDigit() || char.lowercaseChar() == 'k' }
                    if (filtered.length <= 9) {
                        run = filtered
                        runError = if (it.isNotEmpty() && !isValidRun(it)) "RUN no válido" else null
                    }
                },
                label = { Text("RUN") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = RunVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                isError = runError != null,
                supportingText = { runError?.let { Text(it) } }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { datePickerDialog.show() }) {
                Text(if (birthDate.isEmpty()) "Seleccionar Fecha de Nacimiento" else birthDate)
            }
            if (birthDateError != null) {
                Text(birthDateError!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = regionExpanded,
                onExpandedChange = { regionExpanded = !regionExpanded }
            ) {
                OutlinedTextField(
                    value = region,
                    onValueChange = {},
                    label = { Text("Región") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    isError = regionError != null
                )
                ExposedDropdownMenu(
                    expanded = regionExpanded,
                    onDismissRequest = { regionExpanded = false }
                ) {
                    regions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                region = selectionOption
                                communes = ChileanGeographicData.regionsAndCommunes[selectionOption] ?: emptyList()
                                comuna = "" // Reset commune when region changes
                                regionExpanded = false
                                regionError = null
                            }
                        )
                    }
                }
            }
            if (regionError != null) {
                Text(regionError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }


            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = communeExpanded,
                onExpandedChange = { if (communes.isNotEmpty()) communeExpanded = !communeExpanded }
            ) {
                OutlinedTextField(
                    value = comuna,
                    onValueChange = {},
                    label = { Text("Comuna") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = communeExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    enabled = communes.isNotEmpty(),
                    isError = comunaError != null
                )
                ExposedDropdownMenu(
                    expanded = communeExpanded,
                    onDismissRequest = { communeExpanded = false }
                ) {
                    communes.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                comuna = selectionOption
                                communeExpanded = false
                                comunaError = null
                            }
                        )
                    }
                }
            }
            if (comunaError != null) {
                Text(comunaError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = street,
                onValueChange = {
                    street = it
                    streetError = if (!isNonEmpty(it)) "La calle y número son requeridos" else null
                },
                label = { Text("Calle y número") },
                modifier = Modifier.fillMaxWidth(),
                isError = streetError != null,
                supportingText = { streetError?.let { Text(it) } }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onRegisterClick(firstName, lastName, email, password, phone, run, birthDate, region, comuna, street) },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            ) {
                Text("Register")
            }
        }
    }
}
