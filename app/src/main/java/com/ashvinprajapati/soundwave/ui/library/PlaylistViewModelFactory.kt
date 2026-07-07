package com.ashvinprajapati.soundwave.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// Factory because PlaylistViewModel needs playlistId
class PlaylistViewModelFactory(private val playlistId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlaylistViewModel(playlistId) as T
    }
}