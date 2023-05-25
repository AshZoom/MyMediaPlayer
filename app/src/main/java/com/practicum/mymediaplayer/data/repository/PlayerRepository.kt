package com.practicum.mymediaplayer.data.repository

import android.media.MediaPlayer
import com.practicum.mymediaplayer.domain.models.Track

class PlayerRepository(private val mediaPlayerListener: MediaPlayerListener) {

    private val mediaPlayer = MediaPlayer()

    fun start() {
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }

    fun isMediaPlayerPlay(): Boolean {
        return mediaPlayer.isPlaying
    }

    //Действия по подготовке MediaPlayer
    fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayerListener.setStatePrepared()
        }
        mediaPlayer.setOnCompletionListener {
            mediaPlayerListener.setStatePrepared()
        }

    }

    fun onCompletionListener() {
        mediaPlayer.setOnCompletionListener {
            mediaPlayerListener.setStatePrepared()
            mediaPlayerListener.removeHandlersCallbacks()
            mediaPlayerListener.setImagePlay()
            // устанавливаем текущее положение медиаплеера равным 0 млсек (начало трека)
            mediaPlayerListener.setTimeInZero()
        }
    }

    fun releasePlayer() {
        mediaPlayer.release()
    }

    fun resetPlayer() {
        mediaPlayer.reset()
    }

}