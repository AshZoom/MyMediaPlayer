package com.practicum.mymediaplayer.data.repository

import android.content.Context
import com.google.gson.Gson
import com.practicum.mymediaplayer.domain.models.Track
import com.practicum.mymediaplayer.domain.repository.TrackRepository
import com.practicum.mymediaplayer.trackString
import com.practicum.mymediaplayer.PLAYLIST_MAKER_PREFERENCE


const val PLAYERACTIVITY_TRACK_VIEW = "playeractivity_track_view"
const val TRACK = "track"
class TrackRepositoryImpl( context:Context) : TrackRepository {

    val sharedPreferences = context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCE, Context.MODE_PRIVATE)
    val playerActivitytrack=saveTrack(trackString)



    override fun getTrack(): Track{
        val playerActivityTrack=
            sharedPreferences.getString(PLAYERACTIVITY_TRACK_VIEW, null) ?: TRACK
        val gson = Gson()
        val trackPlayerActivity= gson.fromJson(playerActivityTrack, Track::class.java)
        return trackPlayerActivity
    }

    fun saveTrack(track:com.practicum.mymediaplayer.Track) {
        val gson = Gson()
        val trackJson = gson.toJson(track)
        sharedPreferences.edit()
            .putString(PLAYERACTIVITY_TRACK_VIEW, trackJson)
            .apply()
    }


}
