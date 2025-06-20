package com.gallery.musicplayerapp.data.controller

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.gallery.musicplayerapp.domain.model.MusicModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class MusicPlayerState(
    val musicList: List<MusicModel> = emptyList(),
    val currentTrackUri: Uri? = null,
    val isPlaying: Boolean = false,
    var currentPlayBackPosition: Int = 0,
    val duration: Int = 0,
)

class MusicPlayerController(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = MediaPlayer().apply {
        setOnCompletionListener { next() }
    }

    private val _state = MutableStateFlow(MusicPlayerState())
    val state: StateFlow<MusicPlayerState> = _state.asStateFlow()

    private var playbackJob: Job? = null

    fun updateMusicList(list: List<MusicModel>) {
        _state.update { it.copy(musicList = list) }
    }

    fun play(uri: Uri) {
        mediaPlayer?.let { player ->
            try {
                if (player.isPlaying) player.pause()
                player.reset()
                player.setDataSource(context, uri)
                player.prepare()

                val shouldResume = (_state.value.currentTrackUri == uri)
                val position = if (shouldResume) _state.value.currentPlayBackPosition else 0

                player.seekTo(position)
                player.start()

                updateState(uri = uri, isPlaying = true)
                startTrackingPlayback()
            } catch (e: Exception) {
                Log.e("MusicPlayerController", "Error playing music: ${e.message}", e)
            }
        }
    }

    fun pause() {
        mediaPlayer?.takeIf { it.isPlaying }?.let { player ->
            player.pause()
            stop()
            _state.update {
                it.copy(
                    currentPlayBackPosition = player.currentPosition,
                    isPlaying = false
                )
            }
            Log.d("MusicPlayerController", "Paused at position: ${player.currentPosition}")
        }
    }

    fun stop() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) player.pause()
            _state.update {
                it.copy(
                    isPlaying = false,
                    currentPlayBackPosition = 0
                )
            }
        }
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
        _state.update { it.copy(currentPlayBackPosition = position) }
        Log.d("MusicPlayerController", "Seeked to position: $position")
    }

    fun next() {
        val currentUri = _state.value.currentTrackUri ?: return
        val musicList = _state.value.musicList

        val currentIndex = musicList.indexOfFirst { it.uri == currentUri }
        if (currentIndex == -1) {
            Log.e("MusicPlayerController", "Current Index not found in music list.")
            return
        }

        val nextIndex = (currentIndex + 1) % musicList.size
        musicList[nextIndex].uri?.let { nextUri ->
            play(nextUri)
            _state.update { it.copy(currentTrackUri = nextUri) }
        }
    }

    fun previous() {
        val currentUri = _state.value.currentTrackUri ?: return
        val musicList = _state.value.musicList

        val currentIndex = musicList.indexOfFirst { it.uri == currentUri }
        if (currentIndex == -1) {
            Log.e("MusicPlayerController", "Current Index not found in music list.")
            return
        }

        val previousIndex = if (currentIndex > 0) currentIndex - 1 else musicList.size - 1
        musicList[previousIndex].uri?.let { previousUri ->
            play(previousUri)
            _state.update { it.copy(currentTrackUri = previousUri) }
        }
    }

    private fun updateState(uri: Uri, isPlaying: Boolean) {
        mediaPlayer?.let { player ->
            _state.update {
                it.copy(
                    currentTrackUri = uri,
                    isPlaying = isPlaying,
                    currentPlayBackPosition = player.currentPosition,
                    duration = player.duration
                )
            }
        }
        Log.d("MusicPlayerController", "Updated state: ${_state.value}")
    }

    private fun startTrackingPlayback() {
        stopTrackingPlayback()
        playbackJob = CoroutineScope(Dispatchers.Main).launch {
            while (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.currentPosition?.let { position ->
                    _state.update { it.copy(currentPlayBackPosition = position) }
                }
                delay(1000)
            }
        }
    }

    private fun stopTrackingPlayback() {
        playbackJob?.cancel()
    }
}
