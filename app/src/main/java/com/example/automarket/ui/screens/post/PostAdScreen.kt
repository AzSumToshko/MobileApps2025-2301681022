package com.example.automarket.ui.screens.post

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext
import com.example.automarket.data.local.entity.CarEntity
import com.example.automarket.ui.navigation.Screen
import com.example.automarket.ui.screens.components.GradientToolbar
import com.example.automarket.ui.theme.*

private val postBrands = listOf("Изберете марка", "Audi", "BMW", "Ford", "Honda", "Hyundai", "Kia", "Mercedes", "Opel", "Toyota", "Volkswagen")
private val fuelOptions = listOf("Бензин", "Дизел", "Електрически", "Хибрид")
private val transmissionOptions = listOf("Ръчна", "Автоматична")
private val bodyOptions = listOf("Седан", "SUV", "Комби", "Купе", "Хечбек", "Ван")
private val euroOptions = listOf("Euro 3", "Euro 4", "Euro 5", "Euro 6")
private val driveOptions = listOf("Предно", "Задно", "4x4")
private val doorOptions = listOf("2", "3", "4", "5")
private val extrasOptions = listOf("Навигация", "Кожен салон", "Ксенон", "Парктроник", "Камера", "Keyless Entry", "Start/Stop", "Lane Assist")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostAdScreen(navController: NavHostController, vm: PostAdViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var currentStep by remember { mutableIntStateOf(1) }

    // Intercept system back — go back one step instead of closing the screen
    BackHandler(enabled = currentStep > 1) {
        currentStep--
    }
    var brand by remember { mutableStateOf("Изберете марка") }
    var brandExpanded by remember { mutableStateOf(false) }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var selectedFuel by remember { mutableStateOf("Бензин") }
    var selectedTransmission by remember { mutableStateOf("Ръчна") }
    val selectedBodyTypes = remember { mutableStateListOf<String>() }
    var color by remember { mutableStateOf("") }
    var engine by remember { mutableStateOf("") }
    var power by remember { mutableStateOf("") }
    var euro by remember { mutableStateOf("Euro 6") }
    var euroExpanded by remember { mutableStateOf(false) }
    var drive by remember { mutableStateOf("Предно") }
    var driveExpanded by remember { mutableStateOf(false) }
    var doors by remember { mutableStateOf("5") }
    var doorsExpanded by remember { mutableStateOf(false) }
    val selectedExtras = remember { mutableStateListOf<String>() }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            GradientToolbar(
                title = "Публикувай обява",
                showBack = true,
                onBack = { if (currentStep > 1) currentStep-- else navController.popBackStack() }
            )
        },
        containerColor = BackgroundLight
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            StepperRow(currentStep)
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)) {
                when (currentStep) {
                    1 -> Step1Content(brand, brandExpanded, { brand = it }, { brandExpanded = it },
                        model, { model = it }, year, { year = it }, price, { price = it },
                        mileage, { mileage = it }, selectedFuel, { selectedFuel = it },
                        selectedTransmission, { selectedTransmission = it }, selectedBodyTypes)
                    2 -> Step2Content(color, { color = it }, engine, { engine = it }, power, { power = it },
                        euro, euroExpanded, { euro = it }, { euroExpanded = it },
                        drive, driveExpanded, { drive = it }, { driveExpanded = it },
                        doors, doorsExpanded, { doors = it }, { doorsExpanded = it },
                        selectedExtras, description, { description = it })
                    3 -> Step3Content()
                }
            }
            Row(Modifier.fillMaxWidth().background(Color.White).padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (currentStep > 1) {
                    OutlinedButton(onClick = { currentStep-- }, modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Accent)) {
                        Text("Назад", color = Accent)
                    }
                }
                Button(
                    onClick = {
                        if (currentStep < 3) {
                            currentStep++
                        } else {
                            vm.insertCar(CarEntity(
                                brand = brand.takeIf { it != "Изберете марка" } ?: "Неизвестна",
                                model = model.ifEmpty { "Неизвестен" },
                                year = year.toIntOrNull() ?: 2020,
                                price = price.toDoubleOrNull() ?: 0.0,
                                mileage = mileage.toIntOrNull() ?: 0,
                                fuelType = selectedFuel,
                                transmission = selectedTransmission,
                                bodyType = selectedBodyTypes.firstOrNull() ?: "Седан",
                                color = color.ifEmpty { "Неизвестен" },
                                engineSize = engine.ifEmpty { "0.0" },
                                powerHp = power.toIntOrNull() ?: 0,
                                description = description,
                                location = "България"
                            ))
                            Toast.makeText(context, "Обявата е публикувана!", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    },
                    modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Accent)
                ) {
                    Text(if (currentStep == 3) "Публикувай" else "Продължи", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun StepperRow(currentStep: Int) {
    val steps = listOf("Основни", "Детайли", "Снимки")
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(PrimaryDark, PrimaryVariant)))
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, label ->
            val stepNum = index + 1
            val active = stepNum == currentStep
            val done = stepNum < currentStep
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(36.dp).background(if (active || done) Accent else Color.White.copy(0.3f), CircleShape),
                    contentAlignment = Alignment.Center) {
                    if (done) Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    else Text("$stepNum", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(Modifier.height(4.dp))
                Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = if (active || done) Accent else Color.White.copy(0.6f))
            }
            if (index < steps.lastIndex) {
                Box(Modifier.weight(1f).height(2.dp).padding(horizontal = 4.dp)
                    .background(if (stepNum < currentStep) Accent else Color.White.copy(0.3f)))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Step1Content(
    brand: String, brandExpanded: Boolean, onBrand: (String) -> Unit, onBrandExpanded: (Boolean) -> Unit,
    model: String, onModel: (String) -> Unit, year: String, onYear: (String) -> Unit,
    price: String, onPrice: (String) -> Unit, mileage: String, onMileage: (String) -> Unit,
    selectedFuel: String, onFuel: (String) -> Unit, selectedTransmission: String, onTransmission: (String) -> Unit,
    selectedBodyTypes: MutableList<String>
) {
    PostCard("Основна информация") {
        ExposedDropdownMenuBox(brandExpanded, onBrandExpanded) {
            OutlinedTextField(brand, {}, readOnly = true, label = { Text("Марка") },
                modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(12.dp),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(brandExpanded) })
            ExposedDropdownMenu(brandExpanded, { onBrandExpanded(false) }) {
                postBrands.forEach { b -> DropdownMenuItem({ Text(b) }, { onBrand(b); onBrandExpanded(false) }) }
            }
        }
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(model, onModel, label = { Text("Модел") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(year, onYear, label = { Text("Година") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), singleLine = true)
            OutlinedTextField(price, onPrice, label = { Text("Цена (лв.)") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), singleLine = true)
        }
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(mileage, onMileage, label = { Text("Пробег (км)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)
        Spacer(Modifier.height(14.dp))
        LabelBar("Тип гориво"); Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            fuelOptions.forEach { f -> SmallChip(f, f == selectedFuel) { onFuel(f) } }
        }
        Spacer(Modifier.height(14.dp))
        LabelBar("Скоростна кутия"); Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            transmissionOptions.forEach { t -> SmallChip(t, t == selectedTransmission, Modifier.weight(1f)) { onTransmission(t) } }
        }
        Spacer(Modifier.height(14.dp))
        LabelBar("Тип каросерия"); Spacer(Modifier.height(8.dp))
        bodyOptions.chunked(2).forEach { row ->
            Row {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Step2Content(
    color: String, onColor: (String) -> Unit, engine: String, onEngine: (String) -> Unit,
    power: String, onPower: (String) -> Unit, euro: String, euroExpanded: Boolean,
    onEuro: (String) -> Unit, onEuroExpanded: (Boolean) -> Unit, drive: String, driveExpanded: Boolean,
    onDrive: (String) -> Unit, onDriveExpanded: (Boolean) -> Unit, doors: String, doorsExpanded: Boolean,
    onDoors: (String) -> Unit, onDoorsExpanded: (Boolean) -> Unit, selectedExtras: MutableList<String>,
    description: String, onDescription: (String) -> Unit
) {
    PostCard("Технически характеристики") {
        OutlinedTextField(color, onColor, label = { Text("Цвят") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(engine, onEngine, label = { Text("Обем (л)") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), singleLine = true)
            OutlinedTextField(power, onPower, label = { Text("Мощност (к.с.)") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), singleLine = true)
        }
        Spacer(Modifier.height(10.dp))
        SimpleDropdown("Евростандарт", euro, euroExpanded, euroOptions, onEuro, onEuroExpanded)
        Spacer(Modifier.height(10.dp))
        SimpleDropdown("Задвижване", drive, driveExpanded, driveOptions, onDrive, onDriveExpanded)
        Spacer(Modifier.height(10.dp))
        SimpleDropdown("Брой врати", doors, doorsExpanded, doorOptions, onDoors, onDoorsExpanded)
        Spacer(Modifier.height(14.dp))
        LabelBar("Екстри"); Spacer(Modifier.height(8.dp))
        extrasOptions.chunked(2).forEach { row ->
            Row {
                row.forEach { extra ->
                    Row(Modifier.weight(1f).clickable {
                        if (selectedExtras.contains(extra)) selectedExtras.remove(extra) else selectedExtras.add(extra)
                    }, verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(selectedExtras.contains(extra), { if (it) selectedExtras.add(extra) else selectedExtras.remove(extra) },
                            colors = CheckboxDefaults.colors(checkedColor = Accent))
                        Text(extra, fontSize = 13.sp, color = TextPrimary)
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
        Spacer(Modifier.height(14.dp))
        LabelBar("Описание"); Spacer(Modifier.height(8.dp))
        OutlinedTextField(description, onDescription, placeholder = { Text("Опишете автомобила...", color = TextSecondary) },
            modifier = Modifier.fillMaxWidth().height(120.dp), shape = RoundedCornerShape(12.dp), maxLines = 6)
    }
}

@Composable
private fun Step3Content() {
    PostCard("Снимки") {
        Box(Modifier.fillMaxWidth().height(180.dp).border(2.dp, DividerColor, RoundedCornerShape(12.dp)).clickable {},
            contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.CameraAlt, null, tint = Accent, modifier = Modifier.size(52.dp))
                Spacer(Modifier.height(10.dp))
                Text("Добави снимки", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(Modifier.height(4.dp))
                Text("Добавете до 20 снимки от автомобила", fontSize = 12.sp, color = TextSecondary, textAlign = TextAlign.Center)
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(6) {
                Box(Modifier.weight(1f).aspectRatio(1f).border(1.dp, DividerColor, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Add, null, tint = Color.LightGray, modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

@Composable
private fun PostCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(4.dp),
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
private fun LabelBar(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.width(4.dp).height(18.dp).background(Accent, RoundedCornerShape(2.dp)))
        Spacer(Modifier.width(8.dp))
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
}

@Composable
private fun SmallChip(label: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(modifier = modifier
        .background(if (selected) Accent else Color.White, RoundedCornerShape(8.dp))
        .border(1.dp, if (selected) Accent else DividerColor, RoundedCornerShape(8.dp))
        .clickable(onClick = onClick).padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center) {
        Text(label, fontSize = 13.sp, color = if (selected) Color.White else TextPrimary,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleDropdown(label: String, value: String, expanded: Boolean, options: List<String>,
    onValue: (String) -> Unit, onExpanded: (Boolean) -> Unit) {
    ExposedDropdownMenuBox(expanded, onExpanded) {
        OutlinedTextField(value, {}, readOnly = true, label = { Text(label) },
            modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(12.dp),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) })
        ExposedDropdownMenu(expanded, { onExpanded(false) }) {
            options.forEach { opt -> DropdownMenuItem({ Text(opt) }, { onValue(opt); onExpanded(false) }) }
        }
    }
}
