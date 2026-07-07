package com.ashvinprajapati.soundwave.ui.song

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance
import com.ashvinprajapati.soundwave.domain.model.Song
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SongViewModel: ViewModel() {
    var songs = mutableStateListOf<Song>()
        private set

    var isLoading by mutableStateOf(false)
        private set

    var query by mutableStateOf("")

    // Job reference so we can cancel previous search
    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        query = newQuery

        searchJob?.cancel()

        if (newQuery.isBlank()) {
            songs.clear()
            return
        }

        searchJob = viewModelScope.launch {
            delay(3000)
            search(newQuery)
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                val result = RetrofitInstance.api.searchSongs(query)
                songs.clear()
                songs.addAll(result.content)
            }
            catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}