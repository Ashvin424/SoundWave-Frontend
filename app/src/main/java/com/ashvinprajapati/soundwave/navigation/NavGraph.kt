package com.ashvinprajapati.soundwave.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ashvinprajapati.soundwave.ui.login.LoginScreen
import com.ashvinprajapati.soundwave.ui.main.MainScreen

@Composable
fun AppNavigation(navController: NavHostController, isLoggedIn: Boolean) {


    val navController = rememberNavController()

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        } else {
            // ✅ Navigate to home when login succeeds
            navController.navigate("main") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {

        composable("login") {
            LoginScreen(navController)
        }

        composable("main") {
            MainScreen()
        }
    }
}