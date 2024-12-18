package com.gallery.musicplayerapp.presentation.music_list_screen

import android.net.Uri
import com.gallery.musicplayerapp.domain.model.MusicModel

data class MusicListState(
    val musicList: List<MusicModel> = emptyList(),
    val isPlaying: Boolean = false,
    val currentTrackUri: Uri? = null,
    val currentPlayBackPosition: Int = 0
)