package com.ashvinprajapati.soundwave.player

import android.app.Application
import android.content.ComponentName
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.ashvinprajapati.soundwave.domain.model.Song
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val _isShuffled = MutableStateFlow(false)
    val isShuffled = _isShuffled.asStateFlow()

    private val _repeatMode = MutableStateFlow(0)
    val repeatMode = _repeatMode.asStateFlow()

    private var mediaController: MediaController? = null
    private lateinit var controllerFuture: ListenableFuture<MediaController>

    // Current song playing
    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong = _currentSong.asStateFlow()

    //Is music playing right now
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    // Current position in milliseconds
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition = _currentPosition.asStateFlow()

    // current position in millisecond
    private val _duration = MutableStateFlow(0L)
    val duration = _duration.asStateFlow()

    private val _queue = MutableStateFlow<List<Song>>(emptyList())
    val queue = _queue.asStateFlow()

    // Current index in the queue
    private var currentIndex = 0

    //Is mini Player visible
    val isMiniPlayerVisible
        get() = _currentSong.value != null

    init {
        connectToService()
        startPositionTracker()
    }

    // Call this from HomeScreen when songs are loaded
    fun setQueue(songs: List<Song>) {
        _queue.value = songs

        val mediaItem = songs.map { song ->
            MediaItem.Builder()
                .setUri(song.audioUrl)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .setArtworkUri(
                            song.coverImageUrl.let {
                                Uri.parse(it)
                            }
                        )
                        .build()
                )
                .build()

        }
        mediaController?.apply {
            setMediaItems(mediaItem) // ✅ load all at once
            prepare()
        }
    }

    private fun connectToService() {
        val sessionToken = SessionToken(
            getApplication(),
            ComponentName(getApplication(), PlaybackService::class.java)
        )

        controllerFuture = MediaController.Builder(getApplication(), sessionToken).buildAsync()

        // ✅ Use main looper instead of directExecutor
        controllerFuture.addListener({
            try {
                mediaController = controllerFuture.get()
                attachPlayerListener()
            } catch (e: Exception) {
                e.printStackTrace() // this will show if controller failed silently
            }
        }, android.os.Handler(android.os.Looper.getMainLooper())::post)
    }

    private fun attachPlayerListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }

            // ✅ Auto play next song when current one ends
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    playNext()
                }
            }

            // ✅ Sync shuffle state if changed externally
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                _isShuffled.value = shuffleModeEnabled
            }

            // ✅ Sync repeat mode if changed externally
            override fun onRepeatModeChanged(repeatMode: Int) {
                _repeatMode.value = repeatMode
            }

            // ✅ Update current song when ExoPlayer moves to next/prev
            override fun onMediaItemTransition(
                mediaItem: MediaItem?,
                reason: Int
            ) {
                // Find the song that matches current playing media
                val currentTitle = mediaItem?.mediaMetadata?.title?.toString()
                val newSong = _queue.value.find { it.title == currentTitle }
                if (newSong != null) {
                    _currentSong.value = newSong
                }
            }
        })

    }

    // Tracks position every second for the progress bar
    private fun startPositionTracker() {
        viewModelScope.launch {
            while (true) {
                mediaController?.let {
                    _currentPosition.value = it.currentPosition
                    _duration.value = it.duration.coerceAtLeast(0L)
                }
                delay(1000) // update every second
            }
        }
    }

    override fun onCleared() {
        MediaController.releaseFuture(controllerFuture)
        super.onCleared()
    }

    fun playSong(song: Song) {

        val index = _queue.value.indexOfFirst { it.id == song.id }
        if (index != -1) {
            // ✅ Just seek to the right index — no need to setMediaItem again
            mediaController?.seekTo(index, 0)
            mediaController?.play()
            _currentSong.value = song
            _isPlaying.value = true
        }
    }

    fun togglePlayPause() {
        mediaController?.let {
            if (it.isPlaying) it.pause() else it.play()
        }
    }

    fun seekTo(position: Long) {
        mediaController?.seekTo(position)
    }

    fun playNext() {
        if (mediaController?.hasNextMediaItem() == true) {
            mediaController?.seekToNext()
        } else {
            mediaController?.seekTo(0,0)
        }
    }

    fun playPrevious() {
        if ((mediaController?.currentPosition ?: 0) > 3000) {
            mediaController?.seekTo(0)
            return
        }

        if (mediaController?.hasPreviousMediaItem() == true) {
            mediaController?.seekToPrevious()
        } else {
            //seek to last song
            mediaController?.seekTo(queue.value.size - 1, 0)
        }
    }

    fun toggleShuffle() {
        val newValue = !_isShuffled.value
        _isShuffled.value = newValue
        mediaController?.shuffleModeEnabled = newValue
    }

    fun toggleRepeat() {
        val newValue = when (_repeatMode.value) {
            0 -> 1
            1 -> 2
            else -> 0
        }

        _repeatMode.value = newValue
        mediaController?.repeatMode = newValue
    }
}