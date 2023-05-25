package com.practicum.mymediaplayer.data.repository

interface MediaPlayerListener {

    fun setStatePrepared()
    fun removeHandlersCallbacks()
    fun setTimeInZero()
    fun setImagePlay()
}