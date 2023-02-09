package com.practicum.mymediaplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<Button>(R.id.button_arrow_back).setOnClickListener { finish() }


    }
}


