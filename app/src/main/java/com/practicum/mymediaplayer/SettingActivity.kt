package com.practicum.mymediaplayer


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<Button>(R.id.button_arrow_back).setOnClickListener { finish() }

        shareApp()
        writeToSupport()
        userAgreement()

    }

    private fun shareApp() {
        findViewById<ImageView>(R.id.button_share_app).setOnClickListener {
            val context: Context = this
            val messageShareApp = context.getString(R.string.message_share_app)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, messageShareApp)
            shareIntent.type = "text/plain"
            startActivity(shareIntent)
        }

    }

    private fun writeToSupport() {
        findViewById<ImageView>(R.id.button_support).setOnClickListener {
            val context: Context = this
            val messageSupport = context.getString(R.string.messageSupport)
            val messageSupportTheme = context.getString(R.string.messageSupportTheme)
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("alexey-shmakov@yandex.ru"))
            supportIntent.putExtra(Intent.EXTRA_TEXT, messageSupport)
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, messageSupportTheme)
            startActivity(supportIntent)

        }

    }

    private fun userAgreement() {
        findViewById<ImageView>(R.id.button_user_agreement).setOnClickListener {
            val context: Context = this
            val address = Uri.parse(context.getString(R.string.user_agreement_address))
            val agreementIntent = Intent(Intent.ACTION_VIEW, address)
            startActivity(agreementIntent)

        }

    }


}


