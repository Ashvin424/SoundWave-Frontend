package com.ashvinprajapati.soundwave.ui.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance
import com.ashvinprajapati.soundwave.domain.model.Song
import kotlinx.coroutines.launch

class HomeScreenViewModel: ViewModel() {
    private val _songs = mutableStateListOf<Song>()
    val songs: List<Song> = _songs

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