package com.practicum.mymediaplayer.domain.repository

interface PlayerInteractor {

    fun setStatePrepared()
    fun removeHandlersCallbacks()
    fun setTimeInZero()
    fun setImagePlay()
}