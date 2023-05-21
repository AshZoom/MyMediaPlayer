package com.practicum.mymediaplayer.domain.repository

import com.practicum.mymediaplayer.domain.models.Track

interface AudioPlayerViewContract {
    fun showTrackInfo(track: Track)
    fun setPlayButtonState(isPlaying: Boolean)
    fun setPlaybackProgress(progress: String)
}