package com.ashvinprajapati.soundwave.data.remote

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AuthManager {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    fun logout() {
        _isLoggedIn.value = false
    }

    fun login() {
        _isLoggedIn.value = true
    }


}