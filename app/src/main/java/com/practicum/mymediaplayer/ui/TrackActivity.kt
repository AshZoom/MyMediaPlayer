package com.practicum.mymediaplayer.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.practicum.mymediaplayer.R
import com.practicum.mymediaplayer.data.repository.TrackRepositoryImpl
import com.practicum.mymediaplayer.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

class TrackActivity: AppCompatActivity() {

    private lateinit var trackName: TextView
    private lateinit var trackTime: TextView
    private lateinit var artistName: TextView
    private lateinit var albumCover: ImageView
    private lateinit var collectionName: TextView
    private lateinit var collectionNameTitle: TextView
    private lateinit var releaseDate: TextView
    private lateinit var genreName: TextView
    private lateinit var country: TextView
    private lateinit var previewUrl: String
    private lateinit var play: FloatingActionButton
    private lateinit var progress: TextView
    private lateinit var mainThreadHandler: Handler
    private var playerState = STATE_DEFAULT//переменная для хранения текущего состояния MediaPleyer
    private var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        var trackRepository = TrackRepositoryImpl()
        val track = trackRepository.getTrack()

        //Инициализация Handler главного потока.
        mainThreadHandler = Handler(Looper.getMainLooper())
        play = findViewById(R.id.button_play)
        backMenu()
        trackInfo(track)
        preparePlayer()
        play.setOnClickListener {
            playbackControl()
        }
    }

    //Если сворачиваем приложение через Home или запускаем другое приложение,
    //то ставим воспроизведение на паузу
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    //Освобождем все ресурсы и службы, которые система выделяла для воспроизведения аудио
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.reset()
        mainThreadHandler.removeCallbacks(progressTime())
        mediaPlayer.release()
    }

    private fun backMenu() {
        val playerBackMenu = findViewById<MaterialToolbar>(R.id.player_back_menu)
        playerBackMenu.setNavigationOnClickListener {
            finish()
        }
    }

    private fun trackInfo(track: Track) {

        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.artist_name)
        collectionName = findViewById(R.id.album_name)
        releaseDate = findViewById(R.id.release_year_date)
        genreName = findViewById(R.id.genre_name)
        country = findViewById(R.id.country_data)
        trackTime = findViewById(R.id.trackTime)
        albumCover = findViewById(R.id.cover)
        progress = findViewById(R.id.progress)

        Glide
            .with(albumCover)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.album_placeholder)
            .centerCrop()
            .transform(
                RoundedCorners(
                    resources.getDimensionPixelSize(
                        R.dimen.corner_radius_8
                    )
                )
            )
            .into(albumCover)

        trackName.text = track.trackName
        artistName.text = track.artistName
        collectionName.text = track.collectionName
        releaseDate.text = track.releaseDate
        genreName.text = track.primaryGenreName
        country.text = track.country
        previewUrl = track.previewUrl
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)
        if (date != null) {
            val formatDatesString = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            releaseDate.text = formatDatesString
        }
    }

    //Управление Android MediaPlayer
    //Действия по подготовке MediaPlayer
    private fun preparePlayer() {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.setImageResource(R.drawable.icon_play_light)
            mainThreadHandler.removeCallbacks(progressTime())
            playerState = STATE_PREPARED
            //устанавливаем текущее положение медиаплеера равным 0 млсек (начало трека)
            mediaPlayer.seekTo(0)
            //progress?.text = "00:00"
            progress.text =getString(R.string.playing_time)
        }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    //Методы, которые меняют состояние воспроизведения MediaPlayer
    private fun startPlayer() {
        mediaPlayer.start()
        play.setImageResource(R.drawable.icon_pause_light)
        playerState = STATE_PLAYING
        mainThreadHandler.postDelayed(progressTime(), DELAY)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.setImageResource(R.drawable.icon_play_light)
        playerState = STATE_PAUSED
        mainThreadHandler.removeCallbacks(progressTime())
    }

    //Создание Runnable, который будет устанавливать отформатированное текущее значение
    // currentPosition медиаплеера в TextView, а сразу после этого при помощи Handler с
    // задержкой в 300–500 миллисекунд запускать свой экземпляр (this).
    private fun progressTime(): Runnable {
        return object : Runnable {
            override fun run() {
                //проверяем работает ли mediaPlayer.isPlaying
                if (mediaPlayer.isPlaying) {
                    progress.text =
                        (SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(mediaPlayer.currentPosition))
                    mainThreadHandler.postDelayed(this, DELAY)
                } else {
                    mainThreadHandler.removeCallbacks(progressTime())
                }
            }
        }
    }

    //четыре константы для  состояния MediaPlayer
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        // Задержка 500 мсек
        private const val DELAY = 500L
    }
}