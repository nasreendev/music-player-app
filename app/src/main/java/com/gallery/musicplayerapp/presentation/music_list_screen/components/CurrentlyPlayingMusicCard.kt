package com.gallery.musicplayerapp.presentation.music_list_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallery.musicplayerapp.R

@Composable
fun CurrentlyPlayingMusicCard(
    title: String,
    artist: String,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onPlayAndPauseClick: () -> Unit,
    onCloseClick: () -> Unit,
    onCardClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color(0xFFF5F5F5))
            .clickable { onCardClick() }
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.Gray,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .size(30.dp)
                .clickable { onCloseClick() }
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_music),
                    contentDescription = "Album Art",
                    tint = Color.White,
                    modifier = Modifier.size(35.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                AutoScrollingTitle(
                    text = title,
                    isMarquee = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                AutoScrollingTitle(
                    text = artist,
                    isMarquee = false,
                    textSize = 14,
                    textColor = Color.Gray
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .padding(end = 12.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_skip_previous),
                contentDescription = "Previous",
                tint = Color.Black,
                modifier = Modifier
                    .size(35.dp)
                    .clickable { onPreviousClick() }
            )
            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                painter = if (isPlaying) painterResource(id = R.drawable.ic_pause)
                else painterResource(id = R.drawable.ic_play),
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = Color.Black,
                modifier = Modifier
                    .size(38.dp)
                    .clickable { onPlayAndPauseClick() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_skip_next),
                contentDescription = "Next",
                tint = Color.Black,
                modifier = Modifier
                    .size(35.dp)
                    .clickable { onNextClick() }
            )
        }
    }
}


