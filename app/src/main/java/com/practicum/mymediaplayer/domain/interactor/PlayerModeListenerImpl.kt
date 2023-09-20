package com.practicum.mymediaplayer.domain.interactor

import android.media.MediaPlayer
import com.practicum.mymediaplayer.domain.models.Track
import com.practicum.mymediaplayer.domain.repository.PlayerInteractor
import com.practicum.mymediaplayer.domain.repository.PlayerModeListener
import com.practicum.mymediaplayer.utility.Creator

class PlayerModeListenerImpl(private val playerModeListener: PlayerModeListener) :PlayerInteractor{

    private val mediaPlayer = MediaPlayer()

    fun saveTrackClicked(track: Track){
        //trackRepository.saveTrack(track)
    }
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
            playerModeListener.setStatePrepared()
        }
        mediaPlayer.setOnCompletionListener {
            playerModeListener.setStatePrepared()
        }

    }

    fun onCompletionListener() {
        mediaPlayer.setOnCompletionListener {
            playerModeListener.setStatePrepared()
            playerModeListener.removeHandlersCallbacks()
            playerModeListener.setImagePlay()
            // устанавливаем текущее положение медиаплеера равным 0 млсек (начало трека)
            playerModeListener.setTimeInZero()
        }
    }

    fun releasePlayer() {
        mediaPlayer.release()
    }

    fun resetPlayer() {
        mediaPlayer.reset()
    }
    override fun setStatePrepared() {
        playerModeListener.setStatePrepared()
    }

    override fun removeHandlersCallbacks() {
        playerModeListener.removeHandlersCallbacks()
    }

    override fun setTimeInZero() {
        playerModeListener.setTimeInZero()
    }

    override fun setImagePlay() {
        playerModeListener.setImagePlay()
    }
}