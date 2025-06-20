package com.gallery.musicplayerapp.domain.model

import android.net.Uri

data class MusicModel(
    val title: String = "",
    val artist: String = "",
    val id: String = "",
    val uri: Uri,
    val duration: Int = 1,
)