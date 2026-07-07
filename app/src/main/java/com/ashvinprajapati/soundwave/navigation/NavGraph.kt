package com.ashvinprajapati.soundwave.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ashvinprajapati.soundwave.player.PlayerViewModel
import com.ashvinprajapati.soundwave.ui.login.LoginScreen
import com.ashvinprajapati.soundwave.ui.main.MainScreen
import com.ashvinprajapati.soundwave.ui.register.RegisterScreen

@Composable
fun AppNavigation(navController: NavHostController, isLoggedIn: Boolean) {

    val playerViewModel: PlayerViewModel = viewModel()

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
            MainScreen(playerViewModel = playerViewModel)
        }

        composable("register") {
            RegisterScreen(navController)
        }
    }
}