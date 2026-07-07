package com.ashvinprajapati.soundwave.ui.song

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.ashvinprajapati.soundwave.domain.model.Song
import com.ashvinprajapati.soundwave.ui.library.AddToPlaylistSheet

@Composable
fun SongItem(
    song: Song,
    onClick: () -> Unit = {},  // ✅ add this
    onLongPress: (() -> Unit)? = null
) {

    var showAddToPlaylist by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.padding(16.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    onLongPress?.invoke() ?: run {
                        showAddToPlaylist = true
                    }
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val duration = song.duration
        val minutes = duration / 60
        val seconds = duration % 60
        SubcomposeAsyncImage(
            model = song.coverImageUrl,
            contentDescription = "Song Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)),
            content = {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                    is AsyncImagePainter.State.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.DarkGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = "No cover",
                                tint = Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    else -> {
                        SubcomposeAsyncImageContent()
                    }
                }
            }
        )
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = song.title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,                                    // ✅ never wrap to second line
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = song.artist,
                maxLines = 1,                                    // ✅ same for artist
                overflow = TextOverflow.Ellipsis
            )

        }
        Text(
            text = "%d:%02d".format(minutes, seconds),
            fontWeight = FontWeight.Bold,
        )
        IconButton(
            onClick = { onClick() },
        ) {
            Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = "Play Button",
                modifier = Modifier.size(40.dp)
            )
        }
    }

    if (showAddToPlaylist) {
        AddToPlaylistSheet(
            song = song,
            onDismiss = {
                showAddToPlaylist = false
            }
        )
    }
}


