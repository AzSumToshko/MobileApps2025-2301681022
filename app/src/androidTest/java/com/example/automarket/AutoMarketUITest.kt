package com.example.automarket

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.automarket.ui.screens.login.LoginScreen
import com.example.automarket.ui.screens.splash.SplashScreen
import com.example.automarket.ui.theme.AutoMarketTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AutoMarketUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashScreen_showsAppName() {
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            AutoMarketTheme {
                SplashScreen(navController = rememberNavController())
            }
        }
        composeTestRule.onNodeWithText("AutoMarket").assertIsDisplayed()
    }

    @Test
    fun splashScreen_showsTagline() {
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            AutoMarketTheme {
                SplashScreen(navController = rememberNavController())
            }
        }
        composeTestRule.onNodeWithText("Намери колата на мечтите си").assertIsDisplayed()
    }

    @Test
    fun loginScreen_showsLoginButton() {
        composeTestRule.setContent {
            AutoMarketTheme {
                LoginScreen(navController = rememberNavController())
            }
        }
        composeTestRule.onNodeWithText("Вход").assertIsDisplayed()
    }

    @Test
    fun loginScreen_showsRegisterButton() {
        composeTestRule.setContent {
            AutoMarketTheme {
                LoginScreen(navController = rememberNavController())
            }
        }
        composeTestRule.onNodeWithText("Регистрация").assertIsDisplayed()
    }

    @Test
    fun loginScreen_showsContinueWithoutLogin() {
        composeTestRule.setContent {
            AutoMarketTheme {
                LoginScreen(navController = rememberNavController())
            }
        }
        composeTestRule.onNodeWithText("Продължи без вход").assertIsDisplayed()
    }

    @Test
    fun loginScreen_showsAppMarketTitle() {
        composeTestRule.setContent {
            AutoMarketTheme {
                LoginScreen(navController = rememberNavController())
            }
        }
        composeTestRule.onNodeWithText("AutoMarket").assertIsDisplayed()
    }
}
