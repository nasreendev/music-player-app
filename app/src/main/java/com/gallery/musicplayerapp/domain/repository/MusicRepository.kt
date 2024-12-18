package com.gallery.musicplayerapp.domain.repository

import com.gallery.musicplayerapp.domain.model.MusicModel

interface MusicRepository {
    suspend fun getDownloadedMusic(): List<MusicModel>
}