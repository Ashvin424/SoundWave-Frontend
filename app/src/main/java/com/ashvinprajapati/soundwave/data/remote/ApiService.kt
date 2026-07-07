package com.ashvinprajapati.soundwave.data.remote

import com.ashvinprajapati.soundwave.data.remote.dto.ChangePasswordRequest
import com.ashvinprajapati.soundwave.data.remote.dto.LoginRequest
import com.ashvinprajapati.soundwave.data.remote.dto.LoginResponse
import com.ashvinprajapati.soundwave.data.remote.dto.RegisterRequest
import com.ashvinprajapati.soundwave.data.remote.dto.RegisterResponse
import com.ashvinprajapati.soundwave.data.remote.dto.UpdatePlaylistRequest
import com.ashvinprajapati.soundwave.data.remote.dto.UpdateProfileRequest
import com.ashvinprajapati.soundwave.domain.model.PlaylistResponse
import com.ashvinprajapati.soundwave.domain.model.Song
import com.ashvinprajapati.soundwave.domain.model.SongPageResponse
import com.ashvinprajapati.soundwave.domain.model.UserProfile
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ) : Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ) : Response<RegisterResponse>

    @POST("api/songs/{id}/played")
    suspend fun markAsPlayed(
        @Path("id") id: Long
    ) : Response<Unit>

    @POST("api/auth/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ) : Response<String>

    @POST("api/playlists")
    suspend fun createPlaylist(
        @Query("name") name: String
    ) : Response<PlaylistResponse>

    @POST("api/playlists/{playlistId}/songs/{songId}")
    suspend fun addSongToPlaylist(
        @Path("playlistId") playlistId: Long,
        @Path("songId") songId: Long
    ) : Response<Unit>

    @POST("/api/auth/forgot-password")
    suspend fun forgotPassword(
        @Query("email") email : String
    ) : Response<String>

    @GET("api/songs/search")
    suspend fun searchSongs(
        @Query("q") q: String
    ): SongPageResponse

    @GET("api/songs")
    suspend fun getAllSongs() : List<Song>

    @GET("api/users/me")
    suspend fun getProfile() : UserProfile

    @GET("api/songs/recently-played")
    suspend fun getRecentlyPlayed() : List<Song>

    @GET("api/songs/played-count")
    suspend fun getPlayedCount() : Long

    @GET("api/playlists")
    suspend fun getUserPlaylists() : List<PlaylistResponse>


    @DELETE("api/playlists/{playlistId}/songs/{songId}")
    suspend fun removeSongFromPlaylist(
        @Path("playlistId") playlistId: Long,
        @Path("songId") songId: Long
    ) : Response<Unit>

    @DELETE("api/playlists/{playlistId}/delete")
    suspend fun deletePlaylist(
        @Path("playlistId") playlistId: Long
    ) : Response<Unit>

    @PUT("api/auth/update-profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<ResponseBody>

    @PUT("api/playlists/{playlistId}/update")
    suspend fun updatePlaylist(
        @Path("playlistId") playlistId: Long,
        @Body request: UpdatePlaylistRequest
    ): Response<Unit>

}