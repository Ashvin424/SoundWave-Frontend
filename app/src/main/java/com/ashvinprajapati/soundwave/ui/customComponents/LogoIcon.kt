package com.ashvinprajapati.soundwave.ui.customComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LogoIcon() {
    Box(
        modifier = Modifier.size(
            width = 51.dp,
            height = 54.dp
        )
            .clip(
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = Color(0xFF0D59F2),
                shape = RoundedCornerShape(12.dp)
            )

    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(
                    height = 6.dp,
                    width = 3.dp
                )
                    .background(color = Color.White)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Box(
                modifier = Modifier.size(
                    height = 15.dp,
                    width = 3.dp
                )
                    .background(color = Color.White)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Box(
                modifier = Modifier.size(
                    height = 30.dp,
                    width = 3.dp
                )
                    .background(color = Color.White)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Box(
                modifier = Modifier.size(
                    height = 15.dp,
                    width = 3.dp
                )
                    .background(color = Color.White)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Box(
                modifier = Modifier.size(
                    height = 6.dp,
                    width = 3.dp
                )
                    .background(color = Color.White)
            )
        }
    }
}


@Preview
@Composable
private fun LogoIconPrev() {
    LogoIcon()
}