package com.ashvinprajapati.soundwave.ui.song

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance
import com.ashvinprajapati.soundwave.domain.model.Song
import kotlinx.coroutines.launch

class SongViewModel: ViewModel() {
    var songs = mutableStateListOf<Song>()
        private set

    fun search(query: String) {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.searchSongs(query)
                songs.clear()
                songs.addAll(result.content)
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}