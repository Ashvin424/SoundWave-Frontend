package com.ashvinprajapati.soundwave.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashvinprajapati.soundwave.data.remote.AuthManager
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance
import com.ashvinprajapati.soundwave.data.remote.RetrofitInstance.tokenManager
import com.ashvinprajapati.soundwave.data.remote.dto.LoginRequest
import com.ashvinprajapati.soundwave.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<String>("")
    val loginStates: StateFlow<String> = _loginState

    var showForgotPasswordDialog by mutableStateOf(false)
    var forgotPasswordMessage by mutableStateOf("")

    var forgotPasswordSuccess by mutableStateOf(false)





    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(
                    LoginRequest(email, password)
                )

                if (response.isSuccessful) {
                    val token = response.body()?.token
                    tokenManager.saveToken(token!!)
                    UserRepository.fetchProfile()
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

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.forgotPassword(email)
                if (response.isSuccessful) {
                    showForgotPasswordDialog = false
                    forgotPasswordMessage= "Password reset email sent"
                    forgotPasswordSuccess = true
                    } else {
                    forgotPasswordMessage = "Error: ${response.code()}"
                    forgotPasswordSuccess = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}