package com.practicum.mymediaplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class SettingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<Button>(R.id.button_arrow_back).setOnClickListener { finish() }

        findViewById<ImageView>(R.id.button_share_app).setOnClickListener {
            val message="https://practicum.yandex.ru/profile/android-developer/"
            val shareIntent= Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT,message)
            shareIntent.type = "text/plain"
            startActivity(shareIntent)

        }

        findViewById<ImageView>(R.id.button_support).setOnClickListener {
            val message="Спасибо разработчикам и разработчицам за крутое приложение!"
            val messagetheme="Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val shareIntent= Intent(Intent.ACTION_SENDTO)
            shareIntent.data= Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("alexey-shmakov@yandex.ru"))
            shareIntent.putExtra(Intent.EXTRA_TEXT,message)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,messagetheme)
            //shareIntent.type = "text/plain"
            startActivity(shareIntent)

        }

        findViewById<ImageView>(R.id.button_user_agreement).setOnClickListener {
            val address=Uri.parse("https://yandex.ru/legal/practicum_offer/")
            val shareIntent= Intent(Intent.ACTION_VIEW,address)
            startActivity(shareIntent)

        }

    }
}


