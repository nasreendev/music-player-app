package com.gallery.musicplayerapp.data.repository

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.gallery.musicplayerapp.domain.model.MusicModel
import com.gallery.musicplayerapp.domain.repository.MusicRepository

class MusicRepositoryImpl(private val context: Context) : MusicRepository {
    override suspend fun getDownloadedMusic(): List<MusicModel> {
        val musicList = mutableListOf<MusicModel>()
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )

        val selection =
            "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.DATA} NOT LIKE '%Ringtones%' AND ${MediaStore.Audio.Media.DATA} NOT LIKE '%Notifications%' And ${MediaStore.Audio.Media.DURATION} > 3000"


        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )

        cursor?.use {
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION) // New


            while (it.moveToNext()) {
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val id = it.getString(idColumn)
                val path = it.getString(dataColumn)
                val uri = Uri.parse(path)
                val duration = it.getInt(durationColumn)

                if (!title.equals("<unknown>", ignoreCase = true) && !artist.equals(
                        "<unknown>",
                        ignoreCase = true
                    )
                ) {
                    val musicModel = MusicModel(
                        title = title,
                        artist = artist,
                        id = id,
                        uri = uri,
                        duration = duration
                    )
                    musicList.add(musicModel)
                }
            }
        }
        return musicList
    }
}

