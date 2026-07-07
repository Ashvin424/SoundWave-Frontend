package com.ashvinprajapati.soundwave.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ashvinprajapati.soundwave.data.local.ProfileColorManager


class EditProfileViewModelFactory(
    private val colorManager: ProfileColorManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditProfileViewModel(colorManager) as T
    }
}