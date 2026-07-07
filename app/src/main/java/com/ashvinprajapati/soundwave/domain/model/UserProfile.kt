package com.ashvinprajapati.soundwave.domain.model

data class UserProfile(
    val id: Long,
    val fullName: String,
    val email: String,
    val role: String
)
