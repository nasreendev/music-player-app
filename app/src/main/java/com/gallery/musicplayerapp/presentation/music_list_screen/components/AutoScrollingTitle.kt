package com.gallery.musicplayerapp.presentation.music_list_screen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoScrollingTitle(
    modifier: Modifier = Modifier,
    text: String,
    isMarquee: Boolean = false,
) {
    Text(
        text = text,
        fontSize = if (isMarquee) 13.sp else 16.sp,
        fontWeight = FontWeight.Bold,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = if (isMarquee) Modifier.basicMarquee() else Modifier
    )
}