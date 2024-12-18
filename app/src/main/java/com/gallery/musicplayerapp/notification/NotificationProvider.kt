package com.gallery.musicplayerapp.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gallery.musicplayerapp.R
import com.gallery.musicplayerapp.data.controller.MusicPlayerController
import org.koin.core.context.GlobalContext

const val CHANNEL_ID = "MUSIC_PLAYER_CHANNEL_ID"

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val name = "Music Player Channel"
        val descriptionText = "Channel for music player notifications"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@SuppressLint("MissingPermission")
fun showMusicNotification(
    context: Context,
    isPlaying: Boolean,
    songTitle: String,
    artistName: String
) {
    val playPauseIcon = if (isPlaying) {
        R.drawable.ic_pause
    } else {
        R.drawable.ic_play
    }

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_music)
        .setContentTitle(songTitle)
        .setContentText(artistName)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setOngoing(isPlaying)
        .addAction(
            R.drawable.ic_skip_previous,
            "Previous",
            createPreviousIntent(context)
        )
        .addAction(
            playPauseIcon,
            if (isPlaying) "Pause" else "Play",
            createPlayPauseIntent(context, isPlaying)
        )
        .addAction(R.drawable.ic_skip_next, "Next", createNextIntent(context))
        .setDeleteIntent(createDismissIntent(context))
        .build()
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(1, notification)
}


private fun createPreviousIntent(context: Context): PendingIntent {
    val intent = Intent(context, MediaActionReceiver::class.java).apply {
        action = "ACTION_PREVIOUS"
    }
    return PendingIntent.getBroadcast(
        context,
        3,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}

private fun createPlayPauseIntent(context: Context, isPlaying: Boolean): PendingIntent {
    val isPlayingOrPause = if (isPlaying) "ACTION_PAUSE" else "ACTION_PLAY"
    val intent = Intent(context, MediaActionReceiver::class.java).apply {
        action = isPlayingOrPause
    }
    return PendingIntent.getBroadcast(
        context,
        if (isPlaying) 1 else 0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}

private fun createNextIntent(context: Context): PendingIntent {
    val intent = Intent(context, MediaActionReceiver::class.java).apply {
        action = "ACTION_NEXT"
    }
    return PendingIntent.getBroadcast(
        context,
        2,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}

private fun createDismissIntent(context: Context): PendingIntent {
    val intent = Intent(context, MediaActionReceiver::class.java).apply {
        action = "ACTION_DISMISS"
    }
    return PendingIntent.getBroadcast(
        context,
        4,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}

class MediaActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val controller: MusicPlayerController = GlobalContext.get().get<MusicPlayerController>()
        val notificationManager = NotificationManagerCompat.from(context)
        when (action) {
            "ACTION_PLAY" -> controller.playMusic(controller.state.value.currentTrackUri!!)
            "ACTION_PAUSE" -> controller.pauseMusic()
            "ACTION_NEXT" -> controller.playNextTrack()
            "ACTION_PREVIOUS" -> controller.playPreviousTrack()
            "ACTION_DISMISS" -> {
                controller.pauseMusic()
                notificationManager.cancel(1)
            }
        }
    }
}