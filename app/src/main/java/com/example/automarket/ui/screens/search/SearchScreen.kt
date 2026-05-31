package com.example.automarket.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.automarket.ui.navigation.Screen
import com.example.automarket.ui.screens.components.GradientToolbar
import com.example.automarket.ui.theme.*

private val brands = listOf("Всички марки", "Audi", "BMW", "Citroen", "Dacia", "Fiat", "Ford", "Honda", "Hyundai", "Kia", "Mazda", "Mercedes", "Mitsubishi", "Nissan", "Opel", "Peugeot", "Renault", "Seat", "Skoda", "Subaru", "Suzuki", "Toyota", "Volkswagen", "Volvo")
private val fuelTypes = listOf("Всички", "Бензин", "Дизел", "Електрически", "Хибрид")
private val bodyTypes = listOf("Седан", "SUV", "Комби", "Купе", "Хечбек", "Ван")
private val transmissions = listOf("Всички", "Ръчна", "Автоматична")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController, vm: SearchViewModel = hiltViewModel()) {
    var searchText by remember { mutableStateOf("") }
    var selectedBrand by remember { mutableStateOf("Всички марки") }
    var brandExpanded by remember { mutableStateOf(false) }
    var priceFrom by remember { mutableStateOf("") }
    var priceTo by remember { mutableStateOf("") }
    var yearFrom by remember { mutableStateOf("") }
    var yearTo by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf(0f) }
    var selectedFuel by remember { mutableStateOf("Всички") }
    var selectedTransmission by remember { mutableStateOf("Всички") }
    val selectedBodyTypes = remember { mutableStateListOf<String>() }
    val results by vm.results.collectAsState()

    LaunchedEffect(selectedBrand, priceTo, selectedFuel) {
        vm.search(
            selectedBrand.takeIf { it != "Всички марки" },
            priceTo.toDoubleOrNull(),
            selectedFuel.takeIf { it != "Всички" }
        )
    }

    Scaffold(
        topBar = { GradientToolbar("Търсене и Филтри", showBack = true) { navController.popBackStack() } },
        containerColor = BackgroundLight
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Card(Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(4.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(48.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Search, null, tint = Accent)
                    Spacer(Modifier.width(8.dp))
                    androidx.compose.foundation.text.BasicTextField(searchText, { searchText = it }, Modifier.weight(1f),
                        decorationBox = { inner ->
                            if (searchText.isEmpty()) Text("Търси...", color = TextSecondary, fontSize = 14.sp)
                            inner()
                        })
                }
            }

            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FilterCard("Марка и Модел") {
                    ExposedDropdownMenuBox(brandExpanded, { brandExpanded = it }) {
                        OutlinedTextField(selectedBrand, {}, readOnly = true, modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = RoundedCornerShape(12.dp), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(brandExpanded) })
                        ExposedDropdownMenu(brandExpanded, { brandExpanded = false }) {
                            brands.forEach { b -> DropdownMenuItem({ Text(b) }, { selectedBrand = b; brandExpanded = false }) }
                        }
                    }
                }
                FilterCard("Цена") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(priceFrom, { priceFrom = it }, label = { Text("От") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), singleLine = true)
                        Text("лв.", Modifier.align(Alignment.CenterVertically), color = TextSecondary)
                        OutlinedTextField(priceTo, { priceTo = it }, label = { Text("До") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), singleLine = true)
                    }
                }
                FilterCard("Година") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(yearFrom, { yearFrom = it }, label = { Text("От") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), singleLine = true)
                        Text("—", Modifier.align(Alignment.CenterVertically), color = TextSecondary)
                        OutlinedTextField(yearTo, { yearTo = it }, label = { Text("До") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), singleLine = true)
                    }
                }
                FilterCard("Пробег до") {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text("до ${(mileage * 300000).toInt()} км", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Accent)
                    }
                    Slider(mileage, { mileage = it }, colors = SliderDefaults.colors(thumbColor = Accent, activeTrackColor = Accent))
                }
                FilterCard("Тип гориво") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        fuelTypes.forEach { fuel -> SelectableChip(fuel, selectedFuel == fuel) { selectedFuel = fuel } }
                    }
                }
                FilterCard("Тип каросерия") {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        bodyTypes.chunked(2).forEach { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                row.forEach { body ->
                                    Row(Modifier.weight(1f).clickable {
                                        if (selectedBodyTypes.contains(body)) selectedBodyTypes.remove(body) else selectedBodyTypes.add(body)
                                    }, verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(selectedBodyTypes.contains(body), { if (it) selectedBodyTypes.add(body) else selectedBodyTypes.remove(body) },
                                            colors = CheckboxDefaults.colors(checkedColor = Accent))
                                        Text(body, fontSize = 14.sp, color = TextPrimary)
                                    }
                                }
                                if (row.size == 1) Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
                FilterCard("Скоростна кутия") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        transmissions.forEach { tr -> SelectableChip(tr, selectedTransmission == tr, Modifier.weight(1f)) { selectedTransmission = tr } }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            Row(Modifier.fillMaxWidth().background(Color.White).padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(
                    onClick = {
                        selectedBrand = "Всички марки"; priceFrom = ""; priceTo = ""; yearFrom = ""; yearTo = ""
                        mileage = 0f; selectedFuel = "Всички"; selectedTransmission = "Всички"; selectedBodyTypes.clear()
                        vm.search(null, null, null)
                    },
                    modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Accent)
                ) { Text("Изчисти", color = Accent) }
                Button(
                    onClick = { navController.navigate(Screen.Home.route) },
                    modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Accent)
                ) { Text(if (results.isEmpty()) "Покажи резултати" else "${results.size} резултата", color = Color.White) }
            }
        }
    }
}

@Composable
private fun FilterCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column {
            Box(Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(PrimaryDark, PrimaryVariant))).padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Column(Modifier.padding(14.dp), content = content)
        }
    }
}

@Composable
private fun SelectableChip(label: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(modifier = modifier
        .background(if (selected) Accent else Color.White, RoundedCornerShape(8.dp))
        .border(1.dp, if (selected) Accent else DividerColor, RoundedCornerShape(8.dp))
        .clickable(onClick = onClick).padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontSize = 13.sp, color = if (selected) Color.White else TextPrimary,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}
