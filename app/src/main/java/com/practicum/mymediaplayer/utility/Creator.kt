package com.practicum.mymediaplayer.utility

import android.content.Context
import com.practicum.mymediaplayer.App
import com.practicum.mymediaplayer.data.repository.TrackRepositoryImpl
import com.practicum.mymediaplayer.domain.interactor.PlayerInteractorImpl
import com.practicum.mymediaplayer.domain.repository.PlayerInteractor
import com.practicum.mymediaplayer.domain.repository.TrackRepository

object Creator {

    private fun getTrackRepository(context: Context): TrackRepositoryImpl {
        return TrackRepositoryImpl(context)
    }

    fun providePlayerInteractor(context: Context): PlayerInteractor{
        return PlayerInteractorImpl(getTrackRepository(context) )
    }


}