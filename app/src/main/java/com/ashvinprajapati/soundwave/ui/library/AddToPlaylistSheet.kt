package com.ashvinprajapati.soundwave.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashvinprajapati.soundwave.domain.model.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToPlaylistSheet(
    song: Song,
    onDismiss: () -> Unit,
    viewModel : AddToPlayListViewModel = viewModel()
) {
    val sheetState = rememberModalBottomSheetState()

    val addedPlaylists = remember(song.id) { mutableStateListOf<Long>() }

    // ✅ Runs after playlists are fetched
    LaunchedEffect(song.id, viewModel.playlists.size) {
        addedPlaylists.clear()
        val alreadyIn = viewModel.playlists
            .filter { playlist -> playlist.songs.any { it.id == song.id } }
            .map { it.id }
        addedPlaylists.addAll(alreadyIn)
    }

    LaunchedEffect(song.id) {
        viewModel.resetForSong()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp)
        ) {
            Text(
                text = "Add to Playlist",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = song.title,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(16.dp))

            if (viewModel.playlists.isEmpty()) {
                Text(
                    text = "No playlists yet - create one in Library",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else  {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = viewModel.playlists
                    ) { playlist ->
                        val isAdded = viewModel.addedPlaylists.contains(playlist.id)

                        Card(
                            onClick = {
                                if (!addedPlaylists.contains(playlist.id)) {
                                    viewModel.addToPlaylist(playlist.id, song.id)
                                    addedPlaylists.add(playlist.id)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = playlist.name,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Icon(
                                    imageVector = if (addedPlaylists.contains(playlist.id)) Icons.Default.Check else Icons.Default.Add,
                                    contentDescription = null,
                                    tint = if (addedPlaylists.contains(playlist.id)) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}