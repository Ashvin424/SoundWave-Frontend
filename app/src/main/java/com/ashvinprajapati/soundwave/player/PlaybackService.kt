package com.ashvinprajapati.soundwave.player

import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class PlaybackService : MediaSessionService() {

    private var mediaSession : MediaSession? = null

    override fun onCreate() {
        super.onCreate()

        // Create ExoPlayer with audio focus handling
        // Audio focus means: when a call comes in, music pauses automatically
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        val exoPlayer = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true) // pause when headphones disconnected
            .build()

        // MediaSession connects ExoPlayer to the Android media system
        // This enables notification controls, bluetooth controls etc
        mediaSession = MediaSession.Builder(this, exoPlayer).build()
    }

    // This tells Android which MediaSession belongs to this service
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        // Always release player when service is destroyed
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

}