package com.example.automarket.ui.screens.detail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.automarket.ui.screens.components.GradientToolbar
import com.example.automarket.ui.screens.components.brandColor
import com.example.automarket.ui.theme.*

private val extras = listOf(
    "Навигация", "Кожен салон", "Ксенон", "Парктроник",
    "Камера", "Keyless Entry", "Start/Stop", "Lane Assist"
)

@Composable
fun CarDetailScreen(
    navController: NavHostController,
    carId: Int,
    vm: CarDetailViewModel = hiltViewModel()
) {
    val car by vm.car.collectAsState()
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(carId) { vm.loadCar(carId) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Изтриване") },
            text = { Text("Сигурни ли сте, че искате да изтриете тази обява?") },
            confirmButton = {
                TextButton(onClick = { vm.deleteCar { navController.popBackStack() }; showDeleteDialog = false }) {
                    Text("Изтрий", color = Accent)
                }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Отказ") } }
        )
    }

    Scaffold(
        topBar = {
            GradientToolbar(title = "Детайли", showBack = true, onBack = { navController.popBackStack() }) {
                IconButton(onClick = {
                    car?.let {
                        val text = "${it.brand} ${it.model} ${it.year} — ${it.price.toLong()} лв.\n${it.location}"
                        context.startActivity(Intent.createChooser(
                            Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, text) },
                            "Сподели обява"
                        ))
                    }
                }) { Icon(Icons.Default.Share, null, tint = Color.White) }
            }
        },
        containerColor = BackgroundLight
    ) { padding ->
        if (car == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Accent)
            }
            return@Scaffold
        }

        val c = car!!
        val characteristics = listOf(
            "Гориво" to c.fuelType,
            "Скоростна кутия" to c.transmission,
            "Цвят" to c.color,
            "Тип каросерия" to c.bodyType,
            "Двигател" to "${c.engineSize} л.",
            "Мощност" to "${c.powerHp} к.с.",
            "Пробег" to "${c.mileage} км",
            "Локация" to c.location
        )

        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
            Box(
                modifier = Modifier.fillMaxWidth().height(280.dp)
                    .background(Brush.verticalGradient(listOf(brandColor(c.brand), Color.Black)))
            ) {
                Text(c.brand, fontSize = 48.sp, fontWeight = FontWeight.Bold,
                    color = Color.White.copy(0.12f), modifier = Modifier.align(Alignment.Center))
            }

            Card(Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Box(Modifier.fillMaxWidth().padding(16.dp)) {
                    Column {
                        Text("${c.price.toLong()} лв.", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Accent)
                        Spacer(Modifier.height(4.dp))
                        Text("${c.brand} ${c.model}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(Modifier.height(4.dp))
                        Text("${c.year} г. • ${c.location}", fontSize = 14.sp, color = TextSecondary)
                    }
                    IconButton(onClick = { vm.toggleFavorite() }, modifier = Modifier.align(Alignment.TopEnd)) {
                        Icon(if (c.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            null, tint = Accent, modifier = Modifier.size(28.dp))
                    }
                }
            }

            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HighlightCard("${c.engineSize} л.", "Двигател", Icons.Default.Settings, Modifier.weight(1f))
                HighlightCard("${c.powerHp} к.с.", "Мощност", Icons.Default.Speed, Modifier.weight(1f))
                HighlightCard("${c.mileage} км", "Пробег", Icons.Default.Timeline, Modifier.weight(1f))
            }

            Spacer(Modifier.height(8.dp))

            Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Характеристики", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(Modifier.height(12.dp))
                    characteristics.forEachIndexed { i, (label, value) ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(label, fontSize = 14.sp, color = TextSecondary)
                            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                        }
                        if (i < characteristics.lastIndex) HorizontalDivider(color = DividerColor)
                    }
                }
            }

            if (c.description.isNotBlank()) {
                Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Описание", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(Modifier.height(8.dp))
                        Text(c.description, fontSize = 14.sp, color = TextSecondary, lineHeight = 22.sp)
                    }
                }
            }

            Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Екстри", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(Modifier.height(8.dp))
                    extras.chunked(2).forEach { row ->
                        Row(Modifier.fillMaxWidth()) {
                            row.forEach { extra ->
                                Row(Modifier.weight(1f).padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Check, null, tint = SuccessGreen, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text(extra, fontSize = 14.sp, color = TextPrimary)
                                }
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }

            Card(Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(56.dp).background(Accent, CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(28.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Продавач", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("Частно лице", fontSize = 13.sp, color = TextSecondary)
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = {}, modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = Accent)) {
                            Icon(Icons.Default.Phone, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Обади се", color = Color.White)
                        }
                        OutlinedButton(onClick = { showDeleteDialog = true }, modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Accent)) {
                            Icon(Icons.Default.Delete, null, tint = Accent, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Изтрий", color = Accent)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun HighlightCard(value: String, label: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(modifier, shape = RoundedCornerShape(8.dp), elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = Accent, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(label, fontSize = 11.sp, color = TextSecondary)
        }
    }
}
