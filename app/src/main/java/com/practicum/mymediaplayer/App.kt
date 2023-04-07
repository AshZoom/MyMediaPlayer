package com.practicum.mymediaplayer

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {


    override fun onCreate() {

        super.onCreate()
        var darkTheme = false
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCE, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(DARK_THEME_PREFERENCE, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

        )
    }

}







