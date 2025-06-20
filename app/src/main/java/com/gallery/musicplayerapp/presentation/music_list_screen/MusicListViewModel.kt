package com.gallery.musicplayerapp.presentation.music_list_screen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallery.musicplayerapp.data.controller.MusicPlayerController
import com.gallery.musicplayerapp.data.controller.MusicPlayerState
import com.gallery.musicplayerapp.domain.model.MusicModel
import com.gallery.musicplayerapp.domain.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class MusicListState(
    val musicList: List<MusicModel> = emptyList(),
    val isPlaying: Boolean = false,
    val currentTrackUri: Uri? = null,
    val currentPlayBackPosition: Int = 0,
)

class MusicListViewModel(
    private val repository: MusicRepository,
    private val controller: MusicPlayerController,
) : ViewModel() {

    private val _state = MutableStateFlow(MusicListState())
    val state = _state.asStateFlow()

    private val controllerState = controller.state
    init {
        getDownloadedMusic()
        observeMusicPlayerState()
    }

    private fun getDownloadedMusic() {
        viewModelScope.launch {
            try {
                val musicList = repository.getDownloadedMusic()
                _state.update { it.copy(musicList = musicList) }
                controller.updateMusicList(musicList)
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
        controller.play(uri)
    }

    fun pauseMusic() {
        controller.pause()
    }

    fun stopMusic() {
        controller.stop()
    }

    fun seekToPosition(position: Int) {
        controller.seekTo(position)
    }

    fun playNextTrack() {
        controller.next()
    }

    fun playPreviousTrack() {
        controller.previous()
    }
}


