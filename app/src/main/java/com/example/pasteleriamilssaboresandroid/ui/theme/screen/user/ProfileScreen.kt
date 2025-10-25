package com.example.pasteleriamilssaboresandroid.ui.theme.screen.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pasteleriamilssaboresandroid.viewmodel.UserViewModel

@Composable
fun ProfileScreen(userViewModel: UserViewModel) {
    val user by userViewModel.loggedInUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Administrar Perfil", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        user?.let {
            Text(text = "Nombre: ${it.firstName} ${it.lastName}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Email: ${it.email}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Teléfono: ${it.phone}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "RUN: ${it.run}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Fecha de Nacimiento: ${it.birthDate}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Dirección: ${it.street}, ${it.comuna}, ${it.region}")
        }
    }
}

