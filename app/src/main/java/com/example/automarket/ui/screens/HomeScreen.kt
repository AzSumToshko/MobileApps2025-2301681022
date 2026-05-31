package com.example.automarket.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.automarket.ui.navigation.Screen
import com.example.automarket.ui.theme.*

private val categories = listOf("Всички", "Седан", "SUV", "Комби", "Купе", "Електрически", "Хибрид")

@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedCategory by remember { mutableStateOf("Всички") }

    Scaffold(
        topBar = {
            GradientToolbar(title = "AutoMarket") {
                IconButton(onClick = { navController.navigate(Screen.Search.route) }) {
                    Icon(Icons.Default.Search, contentDescription = "Търсене", tint = Color.White)
                }
                IconButton(onClick = { navController.navigate(Screen.Search.route) }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Филтри", tint = Color.White)
                }
            }
        },
        bottomBar = { AutoMarketBottomNav(navController) },
        containerColor = BackgroundLight
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val selected = cat == selectedCategory
                        Box(
                            modifier = Modifier
                                .background(
                                    if (selected) Accent else Color.White,
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { selectedCategory = cat }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                cat,
                                fontSize = 13.sp,
                                color = if (selected) Color.White else TextPrimary,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable { navController.navigate(Screen.Search.route) },
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(3.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(46.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Accent, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("Търси марка, модел...", fontSize = 14.sp, color = TextSecondary)
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .height(150.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.horizontalGradient(listOf(PrimaryDark, Color(0xFF2D1B4E))))
                            .padding(20.dp)
                    ) {
                        Column(modifier = Modifier.align(Alignment.CenterStart)) {
                            Text("Специални предложения", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(Modifier.height(6.dp))
                            Text("Открий изгодни оферти с отстъпка до -30%", fontSize = 13.sp, color = Color.White.copy(alpha = 0.85f))
                            Spacer(Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .background(Accent, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Разгледай →", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                        Row(
                            modifier = Modifier.align(Alignment.BottomEnd),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(modifier = Modifier.width(20.dp).height(4.dp).background(Color.White, RoundedCornerShape(2.dp)))
                            Box(modifier = Modifier.width(8.dp).height(4.dp).background(Color.White.copy(0.5f), RoundedCornerShape(2.dp)))
                            Box(modifier = Modifier.width(8.dp).height(4.dp).background(Color.White.copy(0.5f), RoundedCornerShape(2.dp)))
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem("1 250+", "Обяви")
                    VerticalDivider(modifier = Modifier.height(40.dp), color = DividerColor)
                    StatItem("500+", "Дилъри")
                    VerticalDivider(modifier = Modifier.height(40.dp), color = DividerColor)
                    StatItem("28", "Градове")
                }
            }

            item {
                SectionHeader("Най-нови обяви")
            }

            items(sampleCars) { car ->
                CarCard(car = car) { navController.navigate(Screen.CarDetail.route) }
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Accent)
        Text(label, fontSize = 12.sp, color = TextSecondary)
    }
}
