package com.gallery.musicplayerapp.presentation.music_list_screen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoScrollingTitle(
    modifier: Modifier = Modifier,
    text: String,
    isMarquee: Boolean = false,
    textColor: Color = Color.Black,
    textSize: Int=15
) {
    Text(
        text = text,
        fontSize = textSize.sp,
        fontWeight = FontWeight.Bold,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        color = textColor,
        modifier = modifier.then(
            if (isMarquee) Modifier.basicMarquee() else Modifier
        )
    )
}