package com.practicum.mymediaplayer.domain.repository

interface MediaPlayerListener {

    fun setStatePrepared()
    fun removeHandlersCallbacks()
    fun setTimeInZero()
    fun setImagePlay()
}