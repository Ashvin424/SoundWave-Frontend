package com.ashvinprajapati.soundwave.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ashvinprajapati.soundwave.navigation.BottomNavItem
import com.ashvinprajapati.soundwave.navigation.Routes
import com.ashvinprajapati.soundwave.player.FullPlayerScreen
import com.ashvinprajapati.soundwave.player.MiniPlayer
import com.ashvinprajapati.soundwave.player.PlayerViewModel
import com.ashvinprajapati.soundwave.ui.home.HomeScreen
import com.ashvinprajapati.soundwave.ui.library.LibraryScreen
import com.ashvinprajapati.soundwave.ui.library.PlaylistDetailScreen
import com.ashvinprajapati.soundwave.ui.profile.EditProfileScreen
import com.ashvinprajapati.soundwave.ui.profile.ProfileScreen
import com.ashvinprajapati.soundwave.ui.song.SongSearchScreen

@Composable
fun MainScreen(playerViewModel: PlayerViewModel) {
    val navController = rememberNavController()

    // ✅ Track full player visibility as state instead of navigating from bottomBar
    var showFullPlayer by remember { mutableStateOf(false) }

    // ✅ Show full player as overlay instead of navigation
    if (showFullPlayer) {
        FullPlayerScreen(
            viewModel = playerViewModel,
            onBack = { showFullPlayer = false }
        )
        return // don't render the scaffold underneath
    }

    val item = listOf(
        BottomNavItem(Routes.Home.route, "Home", Icons.Default.Home),
        BottomNavItem(Routes.Search.route, "Search", Icons.Default.Search),
        BottomNavItem(Routes.Library.route, "Library", Icons.Default.LibraryMusic),
        BottomNavItem(Routes.Profile.route, "Profile", Icons.Default.Person)
    )

    Scaffold(
        bottomBar = {
            Column {
                MiniPlayer(viewModel = playerViewModel,
                    onTap = {
                        showFullPlayer = true
                    }
                )
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
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
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.Home.route) {
                HomeScreen(modifier = Modifier.padding(padding), playerViewModel = playerViewModel)
            }
            composable(Routes.Search.route) {
                SongSearchScreen(playerViewModel = playerViewModel)
            }
            composable(Routes.Library.route) {
                LibraryScreen(navController)
            }
            composable(Routes.Profile.route) {
                ProfileScreen(navController)
            }

            composable("editProfile") {
                EditProfileScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable("playlist/{playlistId}") { backStackEntry ->
                val playlistId = backStackEntry.arguments
                    ?.getString("playlistId")
                    ?.toLongOrNull() ?: return@composable

                PlaylistDetailScreen(
                    playlistId = playlistId,
                    playerViewModel = playerViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }

    }
}