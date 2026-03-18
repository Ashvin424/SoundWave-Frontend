package com.ashvinprajapati.soundwave.domain.model

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String? = null, // Nullable if not always provided
    val duration: Long = 0,    // Default value
    val genre: String? = null,
    val coverImageUrl: String,
    val audioUrl: String
)