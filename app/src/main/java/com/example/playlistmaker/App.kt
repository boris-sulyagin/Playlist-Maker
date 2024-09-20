package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
val PLAYLIST_MAKER_THEME_KEY = "dark_theme_key"

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(PLAYLIST_MAKER_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun savedTheme(darkThemeEnabled: Boolean){
        getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
            .edit()
            .putBoolean(PLAYLIST_MAKER_THEME_KEY, darkThemeEnabled)
            .apply()
    }
}