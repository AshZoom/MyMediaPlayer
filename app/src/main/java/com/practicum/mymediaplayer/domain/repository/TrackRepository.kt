package com.practicum.mymediaplayer.domain.repository

import com.practicum.mymediaplayer.domain.models.Track

interface TrackRepository {
    fun getTrack(): Track
    fun saveTrack(track: Track)


}