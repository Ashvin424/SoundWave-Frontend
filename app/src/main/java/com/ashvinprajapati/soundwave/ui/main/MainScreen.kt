package com.ashvinprajapati.soundwave.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ashvinprajapati.soundwave.navigation.BottomNavItem
import com.ashvinprajapati.soundwave.navigation.Routes
import com.ashvinprajapati.soundwave.ui.home.HomeScreen
import com.ashvinprajapati.soundwave.ui.library.LibraryScreen
import com.ashvinprajapati.soundwave.ui.profile.ProfileScreen
import com.ashvinprajapati.soundwave.ui.song.SongSearchScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val item = listOf(
        BottomNavItem(Routes.Home.route, "Home", Icons.Default.Home),
        BottomNavItem(Routes.Search.route, "Search", Icons.Default.Search),
        BottomNavItem(Routes.Library.route, "Library", Icons.Default.LibraryMusic),
        BottomNavItem(Routes.Profile.route, "Home", Icons.Default.Person)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                item.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.routes,
                        onClick = {
                            navController.navigate(item.routes) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(item.icon, contentDescription = item.label)
                        },
                        label = {
                            Text(item.label)
                        }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.Home.route) {
                HomeScreen(modifier = Modifier.padding(padding))
            }
            composable(Routes.Search.route) {
                SongSearchScreen()
            }
            composable(Routes.Library.route) {
                LibraryScreen()
            }
            composable(Routes.Profile.route) {
                ProfileScreen()
            }
        }

    }
}