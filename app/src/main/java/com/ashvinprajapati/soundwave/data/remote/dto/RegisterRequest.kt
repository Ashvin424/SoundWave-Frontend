package com.ashvinprajapati.soundwave.data.remote.dto

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String
)
