package com.ashvinprajapati.soundwave.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashvinprajapati.soundwave.data.remote.AuthManager
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance.tokenManager
import com.ashvinprajapati.soundwave.data.remote.dto.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _registerState = MutableStateFlow("")
    val registerState: StateFlow<String> = _registerState

    fun register(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.register(
                    RegisterRequest(fullName, email, password)
                )
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    tokenManager.saveToken(token!!)
                    AuthManager.login()
                    _registerState.value = "SUCCESS"
                } else {
                    _registerState.value = "Error: ${response.code()}"
                }
            }
             catch (e: Exception) {
                 _registerState.value = "Exception: ${e.message}"
             }
        }
    }
}