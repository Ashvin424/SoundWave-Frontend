package com.ashvinprajapati.soundwave.domain.model

data class SongPageResponse(
    val content: List<Song>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean
)