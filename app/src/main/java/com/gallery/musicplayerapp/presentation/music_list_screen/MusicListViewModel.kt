package com.gallery.musicplayerapp.presentation.music_list_screen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallery.musicplayerapp.data.controller.MusicPlayerController
import com.gallery.musicplayerapp.domain.repository.MusicRepository
import com.gallery.musicplayerapp.presentation.music_player_screen.MusicPlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MusicListViewModel(
    private val repository: MusicRepository,
    private val musicPlayerController: MusicPlayerController
) : ViewModel() {

    private val _state = MutableStateFlow(MusicListState())
    val state = _state.asStateFlow()

    private val controllerState = musicPlayerController.state.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        MusicPlayerState()
    )

    init {
        getDownloadedMusic()
        observeMusicPlayerState()
    }

    fun seekToPosition(position: Int) {
        musicPlayerController.seekToPosition(position)
    }

    fun stopMusic() {
        musicPlayerController.stopMusic()
    }

    fun dismissNotification() {
        musicPlayerController.dismissNotification()
    }

    private fun getDownloadedMusic() {
        viewModelScope.launch {
            try {
                val musicList = repository.getDownloadedMusic()
                Log.d("MusicListViewModel", "Fetched Music List: $musicList")
                _state.update { it.copy(musicList = musicList) }
                musicPlayerController.updateMusicList(musicList)
            } catch (e: Exception) {
                Log.e("MusicListViewModel", "Error fetching downloaded music: ${e.message}", e)
            }
        }
    }

    private fun observeMusicPlayerState() {
        viewModelScope.launch {
            controllerState.collect { controllerState ->
                _state.update {
                    it.copy(
                        isPlaying = controllerState.isPlaying,
                        currentTrackUri = controllerState.currentTrackUri,
                        currentPlayBackPosition = controllerState.currentPlayBackPosition
                    )
                }
            }
        }
    }

    fun playMusic(uri: Uri) {
        musicPlayerController.playMusic(uri)
        _state.update { it.copy(isPlaying = true, currentTrackUri = uri) }
    }

    fun pauseMusic() {
        musicPlayerController.pauseMusic()
        _state.update { it.copy(isPlaying = false) }
    }

    fun playNextTrack() {
        musicPlayerController.playNextTrack()
    }

    fun playPreviousTrack() {
        musicPlayerController.playPreviousTrack()
    }
}


