package com.ashvinprajapati.soundwave.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance
import com.ashvinprajapati.soundwave.data.repository.UserRepository
import com.ashvinprajapati.soundwave.domain.model.Song
import kotlinx.coroutines.launch

class HomeScreenViewModel: ViewModel() {
    private val _songs = mutableStateListOf<Song>()
    val songs: List<Song> = _songs

    private val _recentlyPlayed = mutableStateListOf<Song>()
    val recentlyPlayed: List<Song> = _recentlyPlayed

    var isLoading by mutableStateOf(false)
        private set

    // filter by genres
    val genres: List<String>
        get() = listOf("All") + _songs.map { it.genre ?: "Unknown" }.distinct()
    var selectedGenres by mutableStateOf("All")
    val filteredSongs: List<Song>
        get() = if (selectedGenres == "All") _songs else _songs.filter { it.genre == selectedGenres }

    val userProfile = UserRepository.userProfile

    init {
        viewModelScope.launch {
            UserRepository.fetchProfile() // uses shared repository now
        }
        getAllSongs()
        fetchRecentlyPlayed()
    }

    private fun fetchRecentlyPlayed() {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getRecentlyPlayed()
                _recentlyPlayed.clear()
                _recentlyPlayed.addAll(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun markAsPlayed(songId: Long) {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.markAsPlayed(songId)
                fetchRecentlyPlayed()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun getAllSongs() {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getAllSongs()
                _songs.clear()
                _songs.addAll(result)
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}