package com.ashvinprajapati.soundwave.ui.library

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance
import com.ashvinprajapati.soundwave.domain.model.PlaylistResponse
import kotlinx.coroutines.launch

class PlaylistViewModel(val playlistId: Long) : ViewModel() {

    var playlist by mutableStateOf<PlaylistResponse?>(null)
       private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        fetchPlaylist()
    }

    private fun fetchPlaylist() {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = RetrofitInstance.api.getUserPlaylists()
                playlist = response.find { it.id == playlistId }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun removeSong(songId: Long) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.removeSongFromPlaylist(playlistId, songId)
                fetchPlaylist()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}