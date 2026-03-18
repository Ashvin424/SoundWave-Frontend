package com.ashvinprajapati.soundwave.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashvinprajapati.soundwave.data.remote.AuthManager
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance.tokenManager
import com.ashvinprajapati.soundwave.data.remote.dto.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<String>("")
    val loginStates: StateFlow<String> = _loginState




    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(
                    LoginRequest(email, password)
                )

                if (response.isSuccessful) {
                    val token = response.body()?.token
                    tokenManager.saveToken(token!!)
                    AuthManager.login()
                    _loginState.value = "SUCCESS"
                } else {
                    _loginState.value = "Error: ${response.code()}"
                }
            }
            catch (e: Exception) {
                _loginState.value = "Exception: ${e.message}"
            }
        }
    }
}