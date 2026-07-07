package com.ashvinprajapati.soundwave.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashvinprajapati.soundwave.domain.model.PlaylistResponse
import com.ashvinprajapati.soundwave.domain.model.Song
import com.ashvinprajapati.soundwave.player.PlayerViewModel
import com.ashvinprajapati.soundwave.ui.song.SongItem
import com.ashvinprajapati.soundwave.ui.theme.SoundWaveTheme

@Composable
fun PlaylistDetailScreen(
    playlistId: Long,
    viewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModelFactory(playlistId)),
    playerViewModel: PlayerViewModel,
    onBack: () -> Unit
) {
    PlaylistDetailContent(
        playlist = viewModel.playlist,
        isLoading = viewModel.isLoading,
        onBack = onBack,
        onPlaySong = { playerViewModel.playSong(it) },
        onRemoveSong = { viewModel.removeSong(it) }
    )

}

@Composable
fun PlaylistDetailContent(
    playlist: PlaylistResponse?,
    isLoading: Boolean,
    onBack: () -> Unit,
    onPlaySong: (Song) -> Unit,
    onRemoveSong: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = playlist?.name ?: "PlayList",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${playlist?.songs?.size ?: 0} songs",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(end = 16.dp)
            )
        }

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
            }
        } else if (playlist?.songs.isNullOrEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "No songs yet",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "Add songs from Home or Search screen",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = playlist.songs,
                    key = {it.id}
                ) { song ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                SongItem(
                                    song = song,
                                    onClick = {onPlaySong(song)}
                                )
                            }

                            IconButton(
                                onClick = {
                                    onRemoveSong(song.id)
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove from playlist",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PlaylistDetailPreview() {
    SoundWaveTheme {
        val mockSongs = listOf(
            Song(id = 1, title = "Blinding Lights", artist = "The Weeknd", coverImageUrl = "", audioUrl = "", duration = 200),
            Song(id = 2, title = "Starboy", artist = "The Weeknd", coverImageUrl = "", audioUrl = "", duration = 230),
            Song(id = 3, title = "Save Your Tears asj kjsh", artist = "The Weeknd", coverImageUrl = "", audioUrl = "", duration = 180)
        )
        val mockPlaylist = PlaylistResponse(id = 1, name = "My Favorites", songs = mockSongs)

        PlaylistDetailContent(
            playlist = mockPlaylist,
            isLoading = false,
            onBack = {},
            onPlaySong = {},
            onRemoveSong = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaylistDetailEmptyPreview() {
    SoundWaveTheme {
        PlaylistDetailContent(
            playlist = PlaylistResponse(id = 1, name = "Empty Playlist", songs = emptyList()),
            isLoading = false,
            onBack = {},
            onPlaySong = {},
            onRemoveSong = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaylistDetailLoadingPreview() {
    SoundWaveTheme {
        PlaylistDetailContent(
            playlist = null,
            isLoading = true,
            onBack = {},
            onPlaySong = {},
            onRemoveSong = {}
        )
    }
}

