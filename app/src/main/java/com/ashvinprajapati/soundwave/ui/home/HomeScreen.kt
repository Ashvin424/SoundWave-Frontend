package com.ashvinprajapati.soundwave.ui.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.ashvinprajapati.soundwave.data.local.ProfileColorManager
import com.ashvinprajapati.soundwave.domain.model.Song
import com.ashvinprajapati.soundwave.player.PlayerViewModel
import com.ashvinprajapati.soundwave.ui.song.SongItem
import java.util.Calendar

@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeScreenViewModel = viewModel(),
    playerViewModel: PlayerViewModel
) {

    val user by viewModel.userProfile.collectAsState()
    val firstLetter = user?.fullName?.firstOrNull()?.uppercaseChar() ?: '?'
    val context = LocalContext.current
    val colorManager = remember { ProfileColorManager(context) }
    val profileColor by colorManager.profileColor.collectAsState(
        initial = ProfileColorManager.DEFAULT_COLOR
    )

    val timeBasedGreeting = remember {
        when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good Morning"
            in 12..17 -> "Good AfterNoon"
            else -> "Good Evening"
        }
    }

    LaunchedEffect(viewModel.songs.size) {
        if (viewModel.songs.isNotEmpty()) {
            playerViewModel.setQueue(viewModel.songs.toList()) // toList() creates a snapshot
        }
    }

    PullToRefreshBox(
        isRefreshing = viewModel.isLoading,
        onRefresh = { viewModel.getAllSongs() },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // TopBar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(color = Color(profileColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = firstLetter.toString(),
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(6f)

                    ) {
                        Text(
                            text = "$timeBasedGreeting,",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                        Text(
                            text = user?.fullName ?: "Loading...",
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Recently Played Section
            if (viewModel.recentlyPlayed.isNotEmpty()) {
                item {
                    Text(
                        text = "Recently Played",
                        modifier = Modifier.padding(
                            start = 24.dp, end = 24.dp,
                            top = 16.dp, bottom = 8.dp
                        ),
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.recentlyPlayed) { song ->
                        RecentlyPlayedCard(
                            song = song,
                            onClick = {
                                viewModel.markAsPlayed(song.id)
                                playerViewModel.playSong(song)
                            }
                        )
                    }
                }
            }

            // Genre filter chips
            item {
                Text(
                    text = "Songs",
                    modifier = Modifier.padding(
                        start = 24.dp, end = 24.dp,
                        top = 16.dp, bottom = 8.dp
                    ),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    items(viewModel.genres) { genre ->
                        FilterChip(
                            selected = viewModel.selectedGenres == genre,
                            onClick = { viewModel.selectedGenres = genre },
                            label = { Text(genre) },
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }
            }

            // Songs list — empty state or actual list
            if (viewModel.isLoading) {
                items(5) { ShimmerSongItem() }
            } else if (viewModel.filteredSongs.isEmpty()) {
                item { EmptyState() }
            } else {
                items(viewModel.filteredSongs) { song ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        SongItem(
                            song = song,
                            onClick = {
                                viewModel.markAsPlayed(song.id)
                                playerViewModel.playSong(song)
                            }
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

// Recently played horizontal card
@Composable
fun RecentlyPlayedCard(song: Song, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(130.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(modifier = Modifier.padding(2.dp)) {
            SubcomposeAsyncImage(
                model = song.coverImageUrl,
                contentDescription = song.title,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(110.dp)
                    .clip(RoundedCornerShape(4.dp)),
                loading = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                    }
                },
                error = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.MusicNote, null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )

            Spacer(Modifier.height(6.dp))
            Text(
                text = song.title,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Text(
                text = song.artist,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}

// Empty state
@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
            text = "No songs found",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = "Try a different genre or pull down to refresh",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}

// Shimmer placeholder
@Composable
fun ShimmerSongItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )
            Spacer(Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(11.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f))
            )
        }
    }
}


