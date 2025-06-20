package com.gallery.musicplayerapp.presentation.music_player_screen

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gallery.musicplayerapp.myapp.localNavHostController
import com.gallery.musicplayerapp.presentation.music_list_screen.MusicListViewModel
import com.gallery.musicplayerapp.presentation.music_player_screen.components.MusicPlayer
import com.gallery.musicplayerapp.presentation.music_player_screen.components.PlayerTopBarContent
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    viewModel: MusicListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var selectedMusicUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val navController = localNavHostController.current
    LaunchedEffect(state.currentTrackUri) {
        selectedMusicUri = state.currentTrackUri
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    PlayerTopBarContent(onBackClick = { navController.popBackStack() })
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            selectedMusicUri?.let { uri ->
                val currentMusic = state.musicList.find { it.uri == uri }
                currentMusic?.let { model ->
                    MusicPlayer(
                        playNextTrack = { viewModel.playNextTrack() },
                        playPreviousTrack = { viewModel.playPreviousTrack() },
                        playAndPause = {
                            if (state.isPlaying) {
                                viewModel.pauseMusic()
                            } else {
                                viewModel.playMusic(model.uri)
                            }
                        },
                        title = model.title,
                        artist = model.artist,
                        isPlaying = state.isPlaying,
                        currentPlayBackPosition = state.currentPlayBackPosition,
                        duration = currentMusic.duration,
                        onSeekChanged = { position ->
                            viewModel.seekToPosition(position.toInt())
                        }
                    )
                }
            }
        }
    }
}
