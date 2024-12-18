package com.gallery.musicplayerapp.presentation.music_player_screen

import android.net.Uri
import com.gallery.musicplayerapp.domain.model.MusicModel

data class MusicPlayerState(
    val musicList: List<MusicModel> = emptyList(),
    val currentTrackUri: Uri? = null,
    val isPlaying: Boolean = false,
    var currentPlayBackPosition: Int = 0,
    val currentPosition: Int = 0,
    val duration: Int = 0
)