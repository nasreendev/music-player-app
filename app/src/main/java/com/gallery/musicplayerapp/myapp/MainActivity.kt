package com.gallery.musicplayerapp.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gallery.musicplayerapp.presentation.music_list_screen.MusicListScreen
import com.gallery.musicplayerapp.presentation.music_player_screen.MusicPlayerScreen
import com.gallery.musicplayerapp.presentation.permission_screen.AppContent

val localNavHostController = compositionLocalOf<NavHostController> {
    error("error")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            CompositionLocalProvider(localNavHostController provides navController) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.APP_CONTENT.route
                ) {
                    composable(Screen.APP_CONTENT.route) {
                        AppContent()
                    }
                    composable(Screen.MUSIC_LIST_SCREEN.route) {
                        MusicListScreen()
                    }
                    composable(Screen.MUSIC_PLAYER_SCREEN.route) {
                        MusicPlayerScreen()
                    }
                }
            }
        }
    }
}

enum class Screen(val route: String) {
    MUSIC_LIST_SCREEN("music_list_screen"),
    MUSIC_PLAYER_SCREEN("music_player_screen"),
    APP_CONTENT("app_content")
}



