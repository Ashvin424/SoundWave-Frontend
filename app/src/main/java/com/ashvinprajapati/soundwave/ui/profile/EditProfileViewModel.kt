package com.ashvinprajapati.soundwave.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashvinprajapati.soundwave.data.local.ProfileColorManager
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance
import com.ashvinprajapati.soundwave.data.remote.dto.UpdateProfileRequest
import com.ashvinprajapati.soundwave.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val colorManager: ProfileColorManager
) : ViewModel() {
    val currentUser = UserRepository.userProfile

    var fullName by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var isSuccess by mutableStateOf(false)

    val profileColor = colorManager.profileColor
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileColorManager.DEFAULT_COLOR
        )

    val colorOptions = listOf(
        0xFF0D59F2, // Blue
        0xFF9C27B0, // Purple
        0xFF00897B, // Teal
        0xFFE53935, // Red
        0xFFFF6F00, // Amber
        0xFF43A047, // Green
        0xFFEC407A, // Pink
        0xFF546E7A  // Blue Grey
    )

    init {
        fullName = UserRepository.userProfile.value?.fullName ?: ""
    }

    fun saveColor(color: Long) {
        viewModelScope.launch {
            colorManager.saveColor(color)
        }
    }

    fun saveProfile(onSuccess: () -> Unit) {
        if (fullName.isBlank()) {
            errorMessage = "Name cannot be empty"
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = ""
                val response = RetrofitInstance.api.updateProfile(
                    UpdateProfileRequest(fullName)
                )
                if (response.isSuccessful) {
                    // Refresh shared user data so HomeScreen + ProfileScreen update
                    UserRepository.fetchProfile()
                    isSuccess = true
                    onSuccess()
                } else {
                    errorMessage = "Failed to update profile"
                }
            } catch (e: Exception) {
                errorMessage = "Something went wrong"
            } finally {
                isLoading = false
            }
        }
    }
}