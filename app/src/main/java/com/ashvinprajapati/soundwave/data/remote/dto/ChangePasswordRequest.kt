package com.ashvinprajapati.soundwave.data.remote.dto

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)
