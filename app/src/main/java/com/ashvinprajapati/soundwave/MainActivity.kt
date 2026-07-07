package com.ashvinprajapati.soundwave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.ashvinprajapati.soundwave.data.local.TokenManager
import com.ashvinprajapati.soundwave.data.remote.AuthManager
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance
import com.ashvinprajapati.soundwave.navigation.AppNavigation
import com.ashvinprajapati.soundwave.ui.theme.SoundWaveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        RetrofitInstance.init(applicationContext)
        setContent {
            SoundWaveTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val tokenManager = remember { TokenManager(context) }
                val isLoggedIn by AuthManager.isLoggedIn.collectAsState()

                // Only sync auth state here, no navigation
                LaunchedEffect(isLoggedIn) {
                    val token = tokenManager.getToken()
                    if (token.isNullOrEmpty()) {
                        AuthManager.logout()
                    } else {
                        AuthManager.login()
                    }
                }

                AppNavigation(navController, isLoggedIn)
            }
        }
    }
}