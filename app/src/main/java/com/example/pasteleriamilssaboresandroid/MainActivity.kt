package com.example.pasteleriamilssaboresandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pasteleriamilssaboresandroid.navigation.AppNavHost
import com.example.pasteleriamilssaboresandroid.ui.theme.PasteleriaMilsSaboresAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PasteleriaMilsSaboresAndroidTheme {
                AppNavHost()
            }
        }
    }
}
