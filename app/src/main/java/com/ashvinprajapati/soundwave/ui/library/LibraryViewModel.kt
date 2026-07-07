package com.ashvinprajapati.soundwave.ui.library

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance
import com.ashvinprajapati.soundwave.data.remote.dto.UpdatePlaylistRequest
import com.ashvinprajapati.soundwave.domain.model.PlaylistResponse
import kotlinx.coroutines.launch

class LibraryViewModel : ViewModel() {

    private val _playlists = mutableStateListOf<PlaylistResponse>()
    val playlists: List<PlaylistResponse> = _playlists

    var isLoading by mutableStateOf(false)
        private set

    var showCreateSheet by mutableStateOf(false)

    var showUpdateDialog by mutableStateOf(false)

    var createError by mutableStateOf("")

    var updateError by mutableStateOf("")

    var deleteError by mutableStateOf("")

    var updatePlaylistSuccess by mutableStateOf(false)


    init {
        fetchPlaylists()
    }

    private fun fetchPlaylists() {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = RetrofitInstance.api.getUserPlaylists()
                _playlists.clear()
                _playlists.addAll(response)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun createPlaylist(name: String) {
        if (name.isBlank()) {
            createError = "Playlist name cannot be empty"
            return
        }

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.createPlaylist(name)
                if (response.isSuccessful) {
                    showCreateSheet = false
                    createError = ""
                    fetchPlaylists()
                } else {
                    createError = "Failed to create playlist"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                createError = "Failed to create playlist"
            }
        }
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deletePlaylist(playlistId)
                if (response.isSuccessful) {
                    showCreateSheet = false
                    deleteError = ""
                    fetchPlaylists()
                } else {
                    deleteError = "Failed to delete playlist"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                deleteError = "Something went wrong"
            }
        }
    }

    fun updatePlaylist(playlistId: Long, newName: String) {
        if (newName.isBlank()) {
            updateError = "Playlist name cannot be empty"
            return
        }

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.updatePlaylist(
                    playlistId,
                    UpdatePlaylistRequest(newName)
                )
                if (response.isSuccessful) {
                    showCreateSheet = false
                    showUpdateDialog = false
                    updateError = ""
                    updatePlaylistSuccess = true
                    fetchPlaylists()
                } else {
                    updateError = "Failed to update playlist"
                    updatePlaylistSuccess = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                updateError = "Something went wrong"
            }
        }
    }
}