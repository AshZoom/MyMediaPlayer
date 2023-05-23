package com.practicum.mymediaplayer.domain.repository

import android.media.MediaPlayer
import android.provider.Settings.Global.getString
import com.practicum.mymediaplayer.R
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerPresenter(private val view: AudioPlayerViewContract) : AudioPlayerPresenterContract {

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()

    override fun preparePlayer(previewUrl: String) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            view.setPlayButtonState(true)
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            view.setPlayButtonState(false)
            playerState = STATE_PREPARED
            mediaPlayer.seekTo(0)
            //view.setPlaybackProgress(getString(R.string.playing_time))
        }
    }

    override fun handlePlaybackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        view.setPlayButtonState(true)
        playerState = STATE_PLAYING
        view.setPlaybackProgress(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition))
        //view.updatePlaybackProgress()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        view.setPlayButtonState(false)
        playerState = STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.reset()
        mediaPlayer.release()
    }


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}