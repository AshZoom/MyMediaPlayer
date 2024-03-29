package com.practicum.mymediaplayer.ui


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.practicum.mymediaplayer.R
import com.practicum.mymediaplayer.data.repository.TrackRepositoryImpl
import com.practicum.mymediaplayer.domain.models.Track
import com.practicum.mymediaplayer.presentation.PlayerPresenter
import com.practicum.mymediaplayer.presentation.TrackView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlayerActivity : AppCompatActivity(), TrackView {

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
    private lateinit var presenter: PlayerPresenter


    private val gson = Gson()
    private val playerpresenter = PlayerPresenter(this)

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, PlayerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        // сохраняем в переменной trackClicked данные о нажатом треке
        var trackClicked = gson.fromJson(
            intent.getStringExtra("trackClicked"),
            Track::class.java
        )
        playerpresenter.transferTrackClicked(trackClicked)

        val trackRepository = TrackRepositoryImpl(applicationContext)
        //trackRepository.saveTrack(trackClicked)
        val track = trackRepository.getTrack()

        backMenu()
        trackInfo()
        presenter.preparePlayer(track)
        presenter.onCompletionListener()

        play.setOnClickListener {
            presenter.onClickPlayAndPause()
        }
    }

    //Если сворачиваем приложение через Home или запускаем другое приложение,
    //то ставим воспроизведение на паузу
    override fun onPause() {
        super.onPause()
        presenter.pausePlayer()
    }

    //Освобождем все ресурсы и службы, которые система выделяла для воспроизведения аудио
    override fun onDestroy() {
        super.onDestroy()
        presenter.clearPlayer()
    }

    override fun setProgressTime(time: String) {
        progress.text = time
    }

    private fun backMenu() {
        val playerBackMenu = findViewById<MaterialToolbar>(R.id.player_back_menu)
        playerBackMenu.setNavigationOnClickListener {
            finish()
        }
    }

    private fun trackInfo() {
        val track = gson.fromJson(
            intent.getStringExtra("trackClicked"),
            Track::class.java
        )
        presenter = PlayerPresenter(this)
        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.artist_name)
        collectionName = findViewById(R.id.album_name)
        releaseDate = findViewById(R.id.release_year_date)
        genreName = findViewById(R.id.genre_name)
        country = findViewById(R.id.country_data)
        trackTime = findViewById(R.id.trackTime)
        albumCover = findViewById(R.id.cover)
        progress = findViewById(R.id.progress)
        play = findViewById(R.id.button_play)

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
        trackTime.text = millisecFormat(track)
        val date = dateReleaseFormat(track)
        if (date != null) {
            val formatDatesString = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            releaseDate.text = formatDatesString
        }
    }

    fun millisecFormat(track: Track): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

    fun dateReleaseFormat(track: Track): Date =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)

    override fun setPlayIcon(image: Int) {
        play.setImageResource(image)
    }

}