package com.example.automarket.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object CarDetail : Screen("car_detail")
    data object Favorites : Screen("favorites")
    data object PostAd : Screen("post_ad")
}
