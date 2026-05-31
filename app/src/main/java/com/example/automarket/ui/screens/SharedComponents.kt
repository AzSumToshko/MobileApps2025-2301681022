package com.example.automarket.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.automarket.ui.navigation.Screen
import com.example.automarket.ui.theme.*

data class CarPreview(
    val id: Int,
    val fullTitle: String,
    val yearMileage: String,
    val fuelTransmission: String,
    val price: String,
    val location: String,
    val postedTime: String = "Преди 2 часа",
    val colorPlaceholder: Color = PrimaryDark,
    val isFavorite: Boolean = false
)

val sampleCars = listOf(
    CarPreview(1, "BMW X6 xDrive40d", "2021 • 65 000 км", "Дизел • Автоматик", "89 500 лв.", "Пловдив", colorPlaceholder = Color(0xFF1A3A5C)),
    CarPreview(2, "Mercedes GLE Mansory", "2020 • 45 000 км", "Дизел • Автоматик", "145 000 лв.", "София", colorPlaceholder = Color(0xFF2C2C2C)),
    CarPreview(3, "Audi Q5 2.0 TDI", "2019 • 87 000 км", "Дизел • Автоматик", "62 000 лв.", "Варна", colorPlaceholder = Color(0xFF1C3A1C)),
    CarPreview(4, "Fiat Multipla 1.9 JTD", "2005 • 210 000 км", "Дизел • Ръчна", "4 500 лв.", "Бургас", colorPlaceholder = Color(0xFF3A2020))
)

val toolbarGradient = Brush.horizontalGradient(listOf(PrimaryDark, PrimaryVariant))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradientToolbar(
    title: String,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White) },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        modifier = Modifier.background(toolbarGradient)
    )
}

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home.route, Icons.Default.Home, "Начало"),
    BottomNavItem(Screen.Search.route, Icons.Default.Search, "Търсене"),
    BottomNavItem(Screen.PostAd.route, Icons.Default.Add, "Публикувай"),
    BottomNavItem(Screen.Favorites.route, Icons.Default.Favorite, "Любими"),
    BottomNavItem(Screen.Login.route, Icons.Default.Person, "Профил")
)

@Composable
fun AutoMarketBottomNav(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, fontSize = 10.sp) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Accent,
                    selectedTextColor = Accent,
                    indicatorColor = Accent.copy(alpha = 0.1f),
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}

@Composable
fun CarCard(car: CarPreview, showRemoveButton: Boolean = false, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Brush.verticalGradient(listOf(car.colorPlaceholder, Color.Black)))
            ) {
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart)
                        .background(Accent, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("ТОП", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Row(
                    modifier = Modifier.align(Alignment.TopEnd).padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (showRemoveButton) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Accent,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Color(0xCC000000), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(car.price, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Column(
                    modifier = Modifier.align(Alignment.BottomStart).padding(10.dp)
                ) {
                    Text(car.fullTitle, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        TagBadge(car.yearMileage)
                        TagBadge(car.fuelTransmission)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(car.location, fontSize = 12.sp, color = TextSecondary)
                }
                if (showRemoveButton) {
                    Text("Премахни", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Accent,
                        modifier = Modifier.clickable { })
                } else {
                    Text(car.postedTime, fontSize = 12.sp, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
fun TagBadge(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0x99000000), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text, fontSize = 11.sp, color = Color.White)
    }
}

@Composable
fun SectionHeader(title: String, actionLabel: String = "Виж всички →", onAction: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.width(4.dp).height(22.dp).background(Accent, RoundedCornerShape(2.dp)))
            Spacer(Modifier.width(8.dp))
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
        Text(actionLabel, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Accent,
            modifier = Modifier.clickable(onClick = onAction))
    }
}
