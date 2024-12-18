package com.gallery.musicplayerapp.presentation.music_list_screen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gallery.musicplayerapp.myapp.Screen
import com.gallery.musicplayerapp.myapp.localNavHostController
import com.gallery.musicplayerapp.presentation.music_list_screen.components.CurrentlyPlayingMusicCard
import com.gallery.musicplayerapp.presentation.music_list_screen.components.MusicItemCard
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MusicListScreen(viewModel: MusicListViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()

    var selectedMusicUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var isCurrentlyPlayingVisible by remember {
        mutableStateOf(false)
    }
    val navController = localNavHostController.current
    LaunchedEffect(state.currentTrackUri) {
        selectedMusicUri = state.currentTrackUri
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(state.musicList) { model ->
                MusicItemCard(
                    musicModel = model,
                    onCardClick = { uri ->
                        if (selectedMusicUri == uri && isCurrentlyPlayingVisible) {
                            navController.navigate(Screen.MUSIC_PLAYER_SCREEN.route)
                        } else {
                            selectedMusicUri = uri
                            viewModel.playMusic(uri = uri)
                            isCurrentlyPlayingVisible = true
                        }
                    },
                    isSelected = selectedMusicUri == model.uri,
                )
            }
        }
        Box {
            selectedMusicUri?.let { uri ->
                val currentMusic = state.musicList.find { it.uri == selectedMusicUri }
                currentMusic?.let { model ->
                    CurrentlyPlayingMusicCard(
                        title = model.title,
                        artist = model.artist,
                        isPlaying = state.isPlaying,
                        modifier = Modifier.align(Alignment.BottomCenter),
                        onNextClick = {
                            viewModel.playNextTrack()
                        },
                        onPreviousClick = {
                            viewModel.playPreviousTrack()
                        },
                        onPlayAndPauseClick = {
                            if (state.isPlaying) {
                                viewModel.pauseMusic()
                            } else {
                                viewModel.playMusic(uri)
                            }
                        },
                        onCardClick = {
                            navController.navigate(Screen.MUSIC_PLAYER_SCREEN.route)
                        },
                        onCloseClick = {
                            selectedMusicUri = null
                            viewModel.stopMusic()
                            viewModel.dismissNotification()
                        }
                    )
                }
            }
        }
    }
}

