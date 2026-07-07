package com.ashvinprajapati.soundwave.ui.library

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance
import com.ashvinprajapati.soundwave.domain.model.PlaylistResponse
import kotlinx.coroutines.launch

class AddToPlayListViewModel : ViewModel() {

    private val _playlists = mutableStateListOf<PlaylistResponse>()
    val playlists: List<PlaylistResponse> = _playlists

    // Track which playlists the song was added to — shows checkmark
    val addedPlaylists = mutableStateListOf<Long>()

    init {
        fetchPlaylists()
    }

    fun resetForSong() {
        addedPlaylists.clear()
    }

    private fun fetchPlaylists() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getUserPlaylists()
                _playlists.clear()
                _playlists.addAll(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addToPlaylist(playlistId : Long, songId: Long) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.addSongToPlaylist(playlistId, songId)
                if (response.isSuccessful) {
                    addedPlaylists.add(playlistId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
