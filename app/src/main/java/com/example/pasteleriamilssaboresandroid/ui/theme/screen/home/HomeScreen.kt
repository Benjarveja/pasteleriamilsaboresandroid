package com.example.pasteleriamilssaboresandroid.ui.theme.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ImageSliderPlaceholder()
        Spacer(modifier = Modifier.height(16.dp))
        IntroductionPlaceholder()
        Spacer(modifier = Modifier.height(16.dp))
        RecommendedProductsPlaceholder()
        Spacer(modifier = Modifier.height(16.dp))
        PopularProductsPlaceholder()
        Spacer(modifier = Modifier.height(16.dp))
        CustomerReviewsPlaceholder()
    }
}

@Composable
fun ImageSliderPlaceholder() {
    Card(modifier = Modifier.fillMaxSize().height(200.dp)) {
        Text("Image Slider", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun IntroductionPlaceholder() {
    Card(modifier = Modifier.fillMaxSize()) {
        Text("Introduction", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun RecommendedProductsPlaceholder() {
    Card(modifier = Modifier.fillMaxSize()) {
        Text("Recommended Products", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun PopularProductsPlaceholder() {
    Card(modifier = Modifier.fillMaxSize()) {
        Text("Popular Products", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun CustomerReviewsPlaceholder() {
    Card(modifier = Modifier.fillMaxSize()) {
        Text("Customer Reviews", modifier = Modifier.padding(16.dp))
    }
}

