package com.practicum.mymediaplayer.domain.repository

import com.practicum.mymediaplayer.domain.models.Track

interface PlayerInteractor {

    fun setStatePrepared()
    fun removeHandlersCallbacks()
    fun setTimeInZero()
    fun setImagePlay()

}