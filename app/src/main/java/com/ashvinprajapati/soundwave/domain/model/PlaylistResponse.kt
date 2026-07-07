package com.ashvinprajapati.soundwave.domain.model

data class PlaylistResponse(
    val id: Long,
    val name: String,
    val songs: List<Song>
)