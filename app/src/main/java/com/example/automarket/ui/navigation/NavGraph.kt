package com.example.automarket.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.automarket.ui.screens.CarDetailScreen
import com.example.automarket.ui.screens.FavoritesScreen
import com.example.automarket.ui.screens.HomeScreen
import com.example.automarket.ui.screens.LoginScreen
import com.example.automarket.ui.screens.PostAdScreen
import com.example.automarket.ui.screens.SearchScreen
import com.example.automarket.ui.screens.SplashScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Search.route) { SearchScreen(navController) }
        composable(Screen.CarDetail.route) { CarDetailScreen(navController) }
        composable(Screen.Favorites.route) { FavoritesScreen(navController) }
        composable(Screen.PostAd.route) { PostAdScreen(navController) }
    }
}
