package com.example.automarket.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.automarket.ui.screens.detail.CarDetailScreen
import com.example.automarket.ui.screens.favorites.FavoritesScreen
import com.example.automarket.ui.screens.home.HomeScreen
import com.example.automarket.ui.screens.login.LoginScreen
import com.example.automarket.ui.screens.post.PostAdScreen
import com.example.automarket.ui.screens.search.SearchScreen
import com.example.automarket.ui.screens.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Search.route) { SearchScreen(navController) }
        composable(
            route = Screen.CarDetail.route,
            arguments = listOf(navArgument("carId") { type = NavType.IntType; defaultValue = 0 })
        ) { backStackEntry ->
            CarDetailScreen(navController, backStackEntry.arguments?.getInt("carId") ?: 0)
        }
        composable(Screen.Favorites.route) { FavoritesScreen(navController) }
        composable(Screen.PostAd.route) { PostAdScreen(navController) }
    }
}
