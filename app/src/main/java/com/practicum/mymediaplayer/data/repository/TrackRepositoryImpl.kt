package com.practicum.mymediaplayer.data.repository

import com.practicum.mymediaplayer.domain.models.Track
import com.practicum.mymediaplayer.domain.repository.TrackRepository
import com.practicum.mymediaplayer.trackString

class TrackRepositoryImpl : TrackRepository {
    override fun getTrack(): Track {

        return Track(
            trackId = trackString.trackId,
            trackName = trackString.trackName,
            artistName = trackString.artistName,
            trackTimeMillis = trackString.trackTimeMillis,
            artworkUrl100 = trackString.artworkUrl100,
            collectionName = trackString.collectionName,
            releaseDate = trackString.releaseDate,
            primaryGenreName = trackString.primaryGenreName,
            previewUrl = trackString.previewUrl,
            country = trackString.country
        )
    }
}
