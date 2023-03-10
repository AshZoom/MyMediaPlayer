package com.practicum.mymediaplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Activity для перехода на экран "Поиск"
        //
        val findButton = findViewById<Button>(R.id.button_find)
        findButton.setOnClickListener {
            navigateTo(SearchActivity::class.java)
        }

        //Activity для перехода на экран "Медиатека"
        val mediaButton = findViewById<Button>(R.id.button_media)
        mediaButton.setOnClickListener {
            navigateTo(MediaActivity::class.java)
        }

        //Activity для перехода на экран "Настройки"
        val settingButton = findViewById<Button>(R.id.button_tuning)
        settingButton.setOnClickListener {
            navigateTo(SettingActivity::class.java)
        }


    }

    private fun navigateTo(clazz: Class<out AppCompatActivity>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }
}