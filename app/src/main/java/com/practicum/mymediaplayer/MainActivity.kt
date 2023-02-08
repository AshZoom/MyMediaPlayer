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
            val findIntent=Intent( this, Settingfind::class.java)
            startActivity(findIntent)
        }

        //Activity для перехода на экран "Медиатека"
        val mediaButton = findViewById<Button>(R.id.button_media)
        mediaButton.setOnClickListener {
            val mediaIntent=Intent(this,Media::class.java)
            startActivity(mediaIntent)
        }

        //Activity для перехода на экран "Настройки"
        val settingButton = findViewById<Button>(R.id.button_tuning)
        settingButton.setOnClickListener {
            val settingIntent= Intent (this, Settingactivity::class.java)
            startActivity(settingIntent)
        }

        /*
        find.setOnClickListener {
            Toast.makeText(
                this@MainActivity, "Нажали на кнопку 'Поиск'",
                Toast.LENGTH_SHORT
            ).show()
        }
    val buttonClickListener: View.OnClickListener= object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(
                    this@MainActivity, "Нажали на кнопку 'Медиатека'",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        media.setOnClickListener(buttonClickListener)
        val setting = findViewById<Button>(R.id.button_tuning)
        setting.setOnClickListener {
            Toast.makeText(
                this@MainActivity, "Нажали на кнопку 'Настройки'",
                Toast.LENGTH_SHORT
            ).show()
        }
        */


    }
}