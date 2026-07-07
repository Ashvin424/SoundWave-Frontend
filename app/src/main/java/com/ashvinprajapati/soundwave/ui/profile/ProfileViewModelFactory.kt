package com.ashvinprajapati.soundwave.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ashvinprajapati.soundwave.data.local.TokenManager

class ProfileViewModelFactory(
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(tokenManager) as T
    }
}