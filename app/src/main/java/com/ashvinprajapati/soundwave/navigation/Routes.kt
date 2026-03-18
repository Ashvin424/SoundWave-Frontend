package com.ashvinprajapati.soundwave.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")

    object Main : Routes("main")

    object Home : Routes("home")

    object Search : Routes("search")

    object Library : Routes("library")

    object Profile : Routes("profile")
}