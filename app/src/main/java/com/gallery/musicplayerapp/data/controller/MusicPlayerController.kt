package com.gallery.musicplayerapp.data.controller

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.gallery.musicplayerapp.domain.model.MusicModel
import com.gallery.musicplayerapp.notification.showMusicNotification
import com.gallery.musicplayerapp.presentation.music_player_screen.MusicPlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MusicPlayerController(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = MediaPlayer().apply {
        setOnCompletionListener {
            playNextTrack()
        }
    }
    private val _state = MutableStateFlow(MusicPlayerState())
    val state = _state.asStateFlow()

    private var playbackJob: Job? = null

    fun dismissNotification() {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(1)
    }

    fun stopMusic() {
        mediaPlayer?.apply {
            if (isPlaying) {
                pause()
            }
            _state.update {
                it.copy(
                    isPlaying = false,
                    currentTrackUri = _state.value.currentTrackUri,
                    currentPlayBackPosition = 0
                )
            }
        }
    }

    fun updateMusicList(list: List<MusicModel>) {
        _state.update { it.copy(musicList = list) }
    }

    fun seekToPosition(position: Int) {
        mediaPlayer?.seekTo(position)
        _state.update { it.copy(currentPlayBackPosition = position) }
        Log.d("MusicPlayerController", "Seeked to position: $position")
    }

    fun playMusic(uri: Uri) {
        mediaPlayer?.apply {
            try {
                if (isPlaying) {
                    pause()
                }
                reset()
                setDataSource(context, uri)
                prepare()

                if (_state.value.currentTrackUri == uri) {
                    seekToPosition(_state.value.currentPlayBackPosition)
                } else {
                    seekTo(0)
                }
                start()
                updateState(uri, true)
                startTrackingPlaybackProgress()
                val currentMusic = _state.value.musicList.find { it.uri == uri }
                currentMusic?.let {
                    showMusicNotification(
                        context = context,
                        isPlaying = true,
                        songTitle = currentMusic.title,
                        artistName = currentMusic.artist
                    )
                }
            } catch (e: Exception) {
                Log.e("MusicPlayerController", "Error playing music: ${e.message}", e)
            }
        }
    }

    fun pauseMusic() {
        mediaPlayer?.takeIf { it.isPlaying }?.apply {
            pause()
            stopTrackingPlaybackProgress()
            _state.update { it.copy(currentPlayBackPosition = currentPosition, isPlaying = false) }
            val currentMusic =
                _state.value.musicList.find { it.uri == _state.value.currentTrackUri }
            currentMusic?.let {
                showMusicNotification(
                    context = context,
                    isPlaying = false,
                    songTitle = currentMusic.title,
                    artistName = currentMusic.artist
                )
            }
            Log.d("MusicPlayerController", "Paused at position: $currentPosition")
        }
    }

    fun playNextTrack() {
        val currentState = _state.value
        val musicList = currentState.musicList
        val currentUri = currentState.currentTrackUri ?: return

        val currentIndex = musicList.indexOfFirst { it.uri == currentUri }
        if (currentIndex >= 0) {
            val nextIndex = (currentIndex + 1) % musicList.size
            musicList[nextIndex].uri?.let { playMusic(it) }
            _state.update { it.copy(currentTrackUri = musicList[nextIndex].uri) }
        } else {
            Log.e("MusicPlayerController", "Current Index not found in music list.")
        }
    }

    fun playPreviousTrack() {
        val currentState = _state.value
        val musicList = currentState.musicList
        val currentUri = currentState.currentTrackUri ?: return

        val currentIndex = musicList.indexOfFirst { it.uri == currentUri }
        if (currentIndex >= 0) {
            val previousIndex = if (currentIndex > 0) currentIndex - 1 else musicList.size - 1
            musicList[previousIndex].uri?.let { playMusic(it) }
            _state.update { it.copy(currentTrackUri = musicList[previousIndex].uri) }
        } else {
            Log.e("MusicPlayerController", "Current Index not found in music list.")
        }
    }

    private fun startTrackingPlaybackProgress() {
        stopTrackingPlaybackProgress()
        playbackJob = CoroutineScope(Dispatchers.Main).launch {
            while (mediaPlayer?.isPlaying == true) {
                val currentPosition = mediaPlayer?.currentPosition ?: 0
                _state.update { it.copy(currentPlayBackPosition = currentPosition) }
                delay(1000)
            }
        }
    }

    private fun stopTrackingPlaybackProgress() {
        playbackJob?.cancel()
    }

    private fun updateState(uri: Uri, isPlaying: Boolean) {
        _state.update {
            it.copy(
                currentTrackUri = uri,
                isPlaying = isPlaying,
                currentPlayBackPosition = if (isPlaying) mediaPlayer?.currentPosition
                    ?: 0 else it.currentPlayBackPosition,
                duration = mediaPlayer?.duration ?: 0
            )
        }
        Log.d("MusicPlayerController", "Updated state: $state")
    }
}
