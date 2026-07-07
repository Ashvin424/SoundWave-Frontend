package com.ashvinprajapati.soundwave.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.ashvinprajapati.soundwave.data.local.dataStore

// Extension to create DataStore instance
class ProfileColorManager(private val context: Context) {
    companion object {
        private val COLOR_KEY = longPreferencesKey("profile_color")
        // Default color — your app's blue accent
        const val DEFAULT_COLOR = 0xFF0D59F2
    }

    // Observe color as a Flow — updates UI automatically when changed
    val profileColor: Flow<Long> = context.dataStore.data
        .map { prefs ->
            prefs[COLOR_KEY] ?: DEFAULT_COLOR
        }

    suspend fun saveColor(color: Long) {
        context.dataStore.edit { prefs ->
            prefs[COLOR_KEY] = color
        }
    }
}