package com.practicum.mymediaplayer.domain.repository

interface PlayerModeListener {

    fun setStatePrepared()
    fun removeHandlersCallbacks()
    fun setTimeInZero()
    fun setImagePlay()
}