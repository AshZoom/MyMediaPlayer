package com.practicum.mymediaplayer.domain.models

data class Track(
    val trackId:String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val previewUrl:String,
    val country: String
)




