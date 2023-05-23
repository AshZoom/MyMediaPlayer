package com.practicum.mymediaplayer.domain.repository

interface AudioPlayerPresenterContract {
    fun preparePlayer(previewUrl: String)
    fun handlePlaybackControl()
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
}