package com.ashvinprajapati.soundwave.data.remote

import com.ashvinprajapati.soundwave.data.remote.dto.LoginRequest
import com.ashvinprajapati.soundwave.data.remote.dto.LoginResponse
import com.ashvinprajapati.soundwave.domain.model.Song
import com.ashvinprajapati.soundwave.domain.model.SongPageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ) : Response<LoginResponse>

    @GET("api/songs/search")
    suspend fun searchSongs(
        @Query("q") q: String
    ): SongPageResponse

    @GET("api/songs")
    suspend fun getAllSongs() : List<Song>
}