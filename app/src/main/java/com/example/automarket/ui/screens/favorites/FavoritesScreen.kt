package com.example.automarket.ui.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.automarket.ui.navigation.Screen
import com.example.automarket.ui.screens.components.AutoMarketBottomNav
import com.example.automarket.ui.screens.components.CarCard
import com.example.automarket.ui.screens.components.GradientToolbar
import com.example.automarket.ui.theme.*

@Composable
fun FavoritesScreen(navController: NavHostController, vm: FavoritesViewModel = hiltViewModel()) {
    val favorites by vm.favoriteCars.collectAsState()

    Scaffold(
        topBar = { GradientToolbar(title = "Любими обяви") },
        bottomBar = { AutoMarketBottomNav(navController) },
        containerColor = BackgroundLight
    ) { padding ->
        if (favorites.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(48.dp)) {
                    Icon(Icons.Default.FavoriteBorder, null, tint = Color.LightGray, modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("Нямате запазени обяви", fontSize = 18.sp, color = TextSecondary, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = { navController.navigate(Screen.Home.route) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Accent)) {
                        Text("Разгледай обяви", color = Color.White)
                    }
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(vertical = 8.dp)) {
                items(favorites, key = { it.id }) { car ->
                    CarCard(car = car, showRemoveButton = true) {
                        navController.navigate(Screen.CarDetail.createRoute(car.id))
                    }
                }
            }
        }
    }
}
