package com.practicum.mymediaplayer.domain.interactor

import com.practicum.mymediaplayer.domain.models.Track
import com.practicum.mymediaplayer.domain.repository.MediaPlayerListener
import com.practicum.mymediaplayer.domain.repository.PlayerModeListener

class Interactor(private val playerModeListener: PlayerModeListener) : MediaPlayerListener {

    private val playerInteractor = PlayerInteractor(this)

    fun start() {
        playerInteractor.start()
    }

    fun pause() {
        playerInteractor.pause()
    }


    fun preparePlayer(track: Track) {
        playerInteractor.preparePlayer(track)
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
    }

    fun resetPlayer() {
        playerInteractor.resetPlayer()
    }

    fun getCurrentTime(): Int {
        return playerInteractor.getCurrentTime()
    }

    fun isMediaPlayerPlay(): Boolean {
        return playerInteractor.isMediaPlayerPlay()
    }

    fun onCompletionListener() {
        playerInteractor.onCompletionListener()
    }

    override fun setStatePrepared() {
        playerModeListener.setStatePrepared()
    }

    override fun removeHandlersCallbacks() {
        playerModeListener.removeHandlersCallbacks()
    }

    override fun setImagePlay() {
        playerModeListener.setImagePlay()
    }

    override fun setTimeInZero() {
        playerModeListener.setTimeInZero()
    }

}