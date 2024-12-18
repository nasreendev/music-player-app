package com.gallery.musicplayerapp.di

import com.gallery.musicplayerapp.data.controller.MusicPlayerController
import com.gallery.musicplayerapp.data.repository.MusicRepositoryImpl
import com.gallery.musicplayerapp.domain.repository.MusicRepository
import com.gallery.musicplayerapp.presentation.music_list_screen.MusicListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MusicListViewModel(get(), get())
    }
    single<MusicRepository> {
        MusicRepositoryImpl(get())
    }
    single {
        MusicPlayerController(context = androidContext())
    }
}