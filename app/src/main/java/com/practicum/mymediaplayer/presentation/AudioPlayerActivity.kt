package com.practicum.mymediaplayer.presentation

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
import com.practicum.mymediaplayer.domain.models.Track
import com.practicum.mymediaplayer.domain.repository.AudioPlayerPresenter
import com.practicum.mymediaplayer.domain.repository.AudioPlayerPresenterContract
import com.practicum.mymediaplayer.domain.repository.AudioPlayerViewContract
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity : AppCompatActivity(), AudioPlayerViewContract {

    private lateinit var presenter: AudioPlayerPresenterContract
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
    private lateinit var progress:TextView
    private lateinit var mainThreadHandler:Handler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        //Инициализация Handler главного потока.
        mainThreadHandler = Handler(Looper.getMainLooper())
        play = findViewById(R.id.button_play)

        presenter = AudioPlayerPresenter(this)

        backMenu()
        trackInfo()
        preparePlayer()
        play.setOnClickListener {
            presenter.handlePlaybackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.releasePlayer()
    }

    override fun showTrackInfo(track: Track) {
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

    override fun setPlayButtonState(isPlaying: Boolean) {
        val playButtonIcon = if (isPlaying) R.drawable.icon_pause_light else R.drawable.icon_play_light
        play.setImageResource(playButtonIcon)
    }

    override fun setPlaybackProgress(progress: String) {
        this.progress.text = progress
    }

    private fun backMenu() {
        val playerBackMenu = findViewById<MaterialToolbar>(R.id.player_back_menu)
        playerBackMenu.setNavigationOnClickListener {
            finish()
        }
    }

    private fun trackInfo() {
        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.artist_name)
        collectionName = findViewById(R.id.album_name)
        releaseDate = findViewById(R.id.release_year_date)
        genreName = findViewById(R.id.genre_name)
        country = findViewById(R.id.country_data)
        trackTime = findViewById(R.id.trackTime)
        albumCover = findViewById(R.id.cover)
        progress = findViewById(R.id.progress)
    }

    private fun preparePlayer() {
        presenter.preparePlayer(previewUrl)
    }

    companion object {
        private const val DELAY = 500L
    }


}