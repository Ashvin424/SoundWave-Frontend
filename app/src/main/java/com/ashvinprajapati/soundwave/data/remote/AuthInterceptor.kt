package com.ashvinprajapati.soundwave.data.remote

import android.content.Context
import com.ashvinprajapati.soundwave.data.local.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val tokenManager = TokenManager(context)
        val token = runBlocking {
            tokenManager.getToken()
        }
        val request = chain.request()

        val path = request.url.encodedPath
        if (path.contains("/api/auth/login") ||
            path.contains("/api/auth/register")) {
            return chain.proceed(request)
        }

        val requestBuilder = request.newBuilder()

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader(
                "Authorization",
                "Bearer $token"
            )
        }

        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401) {
            runBlocking {
                tokenManager.clearToken()
            }
            AuthManager.logout()
        }

        return response
    }

}