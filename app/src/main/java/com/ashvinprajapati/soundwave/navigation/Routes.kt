package com.ashvinprajapati.soundwave.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")

    object Main : Routes("main")

    object Home : Routes("home")

    object Search : Routes("search")

    object FullPlayerScreen : Routes("fullPlayer")

    object Library : Routes("library")

    object PlaylistDetail : Routes("playlist/{playlistId}")

    object Profile : Routes("profile")

    object Register : Routes("register")
}