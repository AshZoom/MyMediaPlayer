package com.practicum.mymediaplayer

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false

    override fun onCreate() {

        super.onCreate()

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCE, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(PLAYLIST_MAKER_PREFERENCE, false)
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







