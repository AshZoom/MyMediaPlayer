package com.practicum.mymediaplayer.domain.repository

import com.practicum.mymediaplayer.data.repository.MediaPlayerListener
import com.practicum.mymediaplayer.data.repository.PlayerRepository
import com.practicum.mymediaplayer.domain.models.Track

class Interactor(private val playerModeListener: PlayerModeListener) : MediaPlayerListener {

    private val playerRepository = PlayerRepository(this)

    fun start() {
        playerRepository.start()
    }

    fun pause() {
        playerRepository.pause()
    }


    fun preparePlayer(track: Track) {
        playerRepository.preparePlayer(track)
    }

    fun releasePlayer() {
        playerRepository.releasePlayer()
    }

    fun resetPlayer() {
        playerRepository.resetPlayer()
    }

    fun getCurrentTime(): Int {
        return playerRepository.getCurrentTime()
    }

    fun isMediaPlayerPlay(): Boolean {
        return playerRepository.isMediaPlayerPlay()
    }

    fun onCompletionListener() {
        playerRepository.onCompletionListener()
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