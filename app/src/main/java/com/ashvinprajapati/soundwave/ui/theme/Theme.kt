package com.ashvinprajapati.soundwave.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BlueAccent,        // Your main action color
    onPrimary = White,           // Text on top of buttons
    background = DeepNavy,       // Main background
    surface = DeepNavy,          // Card backgrounds
    onBackground = White,        // General text color
    onSurface = White,           // Text on cards
    secondary = BlueAccent.copy(alpha = 0.7f)
)


@Composable
fun SoundWaveTheme(
    // We remove the darkTheme and dynamicColor parameters or set them to defaults
    // that don't affect the internal logic anymore.
    content: @Composable () -> Unit
) {
    // We bypass all logic and strictly use your DarkColorScheme
    val colorScheme = DarkColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}