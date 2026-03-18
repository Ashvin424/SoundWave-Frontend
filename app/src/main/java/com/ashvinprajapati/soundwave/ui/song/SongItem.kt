package com.ashvinprajapati.soundwave.ui.song

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.ashvinprajapati.soundwave.domain.model.Song

@Composable
fun SongItem(
    song: Song
) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val duration = song.duration
        val minutes = duration / 60
        val seconds = duration % 60
        SubcomposeAsyncImage(
            model = song.coverImageUrl,
            contentDescription = "Song Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(40.dp)
                .clip(
                    shape = RoundedCornerShape(10.dp)
                )
                .border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
                .padding(2.dp),

            loading = {
                CircularProgressIndicator()
            }

        )
        Spacer(Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.Center
        ) {

            Text(song.title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(song.artist)

        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "$minutes : $seconds",
            fontWeight = FontWeight.Bold,
        )
        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = "Play Button",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}


