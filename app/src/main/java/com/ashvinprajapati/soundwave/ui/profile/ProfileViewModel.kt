package com.ashvinprajapati.soundwave.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashvinprajapati.soundwave.data.local.TokenManager
import com.ashvinprajapati.soundwave.data.remote.AuthManager
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance
import com.ashvinprajapati.soundwave.data.remote.dto.ChangePasswordRequest
import com.ashvinprajapati.soundwave.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    // Get user from shared repository — no extra API call needed

    var playedCount by mutableLongStateOf(0L)
        private set

    //change password dialog
    var showChangePasswordDialog by mutableStateOf(false)
    var changePasswordSuccess by mutableStateOf(false)
    var changePasswordError by mutableStateOf("")

    init {
        fetchPlayedCount()
    }

    private fun fetchPlayedCount() {
        viewModelScope.launch {
            try {
                playedCount = RetrofitInstance.api.getPlayedCount()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun changePassword(current: String, new: String, confirm: String) {
        if(current.isBlank() || new.isBlank() || confirm.isBlank()) {
            changePasswordError = "Fields cannot be empty"
            return
        }

        if (new.length < 6) {
            changePasswordError = "New password must be at least 6 characters"
            return
        }

        if (new != confirm) {
            changePasswordError = "New password and confirm password do not match"
            return
        }

        if (current == new) {
            changePasswordError = "New password cannot be the same as the current password"
            return
        }

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.changePassword(
                    ChangePasswordRequest(current, new)
                )

                if (response.isSuccessful) {
                    changePasswordSuccess = true
                    changePasswordError = ""
                } else {
                    changePasswordError = "Current password is incorrect"
                }
            } catch (e: Exception) {
                changePasswordError = "Something went wrong"
                e.printStackTrace()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearToken() // clears token from local storage
            UserRepository.clear() // clears user profile from shared repository
            AuthManager.logout() // triggers navigation to login automatically
        }
    }
}