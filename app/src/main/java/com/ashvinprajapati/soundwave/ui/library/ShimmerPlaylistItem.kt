package com.ashvinprajapati.soundwave.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ashvinprajapati.soundwave.ui.theme.SoundWaveTheme

@Composable
fun ShimmerPlaylistItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clip(shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(10.dp)
                )
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(14.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
            Spacer(Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(11.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Preview
@Composable
private fun ShimmerPlaylistItemPrev() {
    SoundWaveTheme { ShimmerPlaylistItem() }
}
