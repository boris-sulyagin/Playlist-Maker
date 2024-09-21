package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(Constants.PLAYLIST_MAKER_PREFS, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(Constants.DARK_THEME_KEY, false)
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
        getSharedPreferences(Constants.PLAYLIST_MAKER_PREFS, MODE_PRIVATE)
            .edit()
            .putBoolean(Constants.DARK_THEME_KEY, darkThemeEnabled)
            .apply()
    }
}