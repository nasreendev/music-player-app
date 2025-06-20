package com.gallery.musicplayerapp.presentation.music_player_screen.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallery.musicplayerapp.R

@Composable
fun MusicPlayer(
    playNextTrack: () -> Unit,
    playPreviousTrack: () -> Unit,
    playAndPause: () -> Unit,
    title: String,
    artist: String,
    isPlaying: Boolean,
    currentPlayBackPosition: Int,
    duration: Int,
    onSeekChanged: (Float) -> Unit
) {
    Log.d("SliderValue", "Current Play Back Position: $currentPlayBackPosition")

    Box(
        modifier = Modifier
            .size(200.dp)
            .background(
                Color.Gray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_music),
            contentDescription = "",
            modifier = Modifier.size(100.dp),
            tint = Color.White
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = title,
        fontSize = 16.sp,
        color = Color.Black,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 4.dp)
    )

    Text(
        text = artist,
        fontSize = 16.sp,
        color = Color.LightGray,
        modifier = Modifier.padding(top = 4.dp)
    )

    Spacer(modifier = Modifier.height(16.dp))
    Column {
        Slider(
            value = currentPlayBackPosition.toFloat(),
            onValueChange = { position ->
                onSeekChanged(position)
            },
            valueRange = 0f..duration.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFFADD8E6),
                activeTrackColor = Color(0xFFADD8E6),
                inactiveTrackColor = Color.LightGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = formatTime(currentPlayBackPosition), color = Color.Black)
            Text(text = formatTime(duration), color = Color.Black)
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = { playPreviousTrack.invoke() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_skip_previous),
                contentDescription = "Previous",
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
        IconButton(onClick = { playAndPause.invoke() }) {
            Icon(
                painter = if (isPlaying) painterResource(id = R.drawable.ic_pause) else painterResource(
                    id = R.drawable.ic_play
                ),
                contentDescription = if (isPlaying) "Pause" else "Play",
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
        IconButton(onClick = { playNextTrack.invoke() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_skip_next),
                contentDescription = "Next",
            )
        }
    }
}

fun formatTime(milliseconds: Int): String {
    val seconds = (milliseconds / 1000) % 60
    val minutes = (milliseconds / (1000 * 60)) % 60
    return String.format("%02d:%02d", minutes, seconds)
}