package com.ashvinprajapati.soundwave.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeScreenViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getAllSongs()
    }
    Column(
        modifier = modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Image(
                imageVector = Icons.Default.Person,
                modifier = Modifier.clip(
                        shape = CircleShape
                    )
                    .background(
                        color = Color.Gray,
                        shape = CircleShape
                    )
                    .size(40.dp)
                    .weight(1f),
                contentDescription = "Profile Image"
            )
            Spacer(Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(6f)

            ) {
                Text(
                    text = "Welcome Back",
                    color = Color.Gray
                )
                Text(
                    text = "Ashvin Prajapati",
                    color = Color.Black,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(
                onClick = {/*TODO*/},
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    modifier = Modifier.size(32.dp),
                    contentDescription = "Setting Btn",
                    tint = Color(0xFF0D59F2)
                )
            }
        }
        Text(
            text = "Songs",
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 0.dp),
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold
        )
        LazyColumn(
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 16.dp
            )
        ) {
            items(viewModel.songs) { song ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(song.title, fontWeight = FontWeight.Bold)

                        Text(song.artist)

                    }
                }

            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPrev() {
    HomeScreen(modifier = Modifier)
}