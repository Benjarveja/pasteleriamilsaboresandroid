package com.example.pasteleriamilssaboresandroid.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginScreen(
    authVM: AuthViewModel,
    onLoggedIn: () -> Unit,
) {
    val ui by authVM.ui.collectAsStateWithLifecycle()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    // Si ya hay usuario, salir
    LaunchedEffect(ui.user) {
        if (ui.user != null) onLoggedIn()
    }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Top) {
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.size(12.dp))
        if (ui.error != null) {
            Text(ui.error ?: "Error", color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.size(8.dp))
        }
        OutlinedTextField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Correo") })
        Spacer(Modifier.size(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Contraseña") })
        Spacer(Modifier.size(16.dp))
        Button(onClick = { authVM.login(email, password) }, enabled = !ui.isLoading, modifier = Modifier.fillMaxWidth()) {
            Text("Ingresar")
        }
        Spacer(Modifier.size(8.dp))
        OutlinedButton(onClick = {
            email = "cliente@milsabores.cl"
            password = "MilSabores123"
            authVM.login(email, password)
        }, enabled = !ui.isLoading, modifier = Modifier.fillMaxWidth()) {
            Text("Usar cuenta demo")
        }
    }
}

