package com.ashvinprajapati.soundwave.data.repository

import android.util.Log
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance
import com.ashvinprajapati.soundwave.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// why Object - because i kotlin objects are singleton
// Singleton - onr instance shared across all ViewModels
object UserRepository {
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile = _userProfile.asStateFlow()

    // call only once when app launch or after login
    suspend fun fetchProfile() {
        try {
            val profile = RetrofitInstance.api.getProfile()
            Log.d("UserRepository", "Profile fetched: ${profile.fullName}")
            _userProfile.value = profile
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // call on logout
    fun clear() {
        _userProfile.value = null
    }
}