package com.practicum.mymediaplayer.presentation

import android.os.Handler
import android.os.Looper
import com.practicum.mymediaplayer.R
import com.practicum.mymediaplayer.domain.models.Track
import com.practicum.mymediaplayer.domain.interactor.Interactor
import com.practicum.mymediaplayer.domain.repository.PlayerModeListener
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerModeListenerImpl(val view: TrackView) : PlayerModeListener {


    private val interactor = Interactor(this)
    val mainThreadHandler = Handler(Looper.getMainLooper())
    private var playerState = STATE_DEFAULT
    private var play = R.drawable.icon_play_light
    private val pause = R.drawable.icon_pause_light


    //Создание Runnable, который будет устанавливать отформатированное текущее значение
    // currentTime медиаплеера , а сразу после этого при помощи Handler с
    // задержкой в 300–500 миллисекунд запускать свой экземпляр (this).
    private val progressTime = object : Runnable {
        override fun run() {
            //val currentTime =TimeFormatUtil()
            // SimpleDateFormat("mm:ss", Locale.getDefault()).format(interactor.getCurrentTime())
            //view.setProgressTime(currentTime)
            val viewRef: WeakReference<TrackView> = WeakReference(view)
            val currentTime = TimeFormatUtil()
            viewRef.get()?.setProgressTime(currentTime)
            mainThreadHandler.postDelayed(this, DELAY)
        }
    }

    fun onClickPlayAndPause() {
        playbackControl()
        progressTimeControl()
    }


    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun progressTimeControl() {
        when (playerState) {
            STATE_PLAYING -> {
                mainThreadHandler.postDelayed(
                    progressTime, DELAY
                )
            }
            STATE_PAUSED -> {
                mainThreadHandler.removeCallbacks(progressTime)
            }
        }
    }

    fun onCompletionListener() {
        interactor.onCompletionListener()
    }

    //Методы, которые меняют состояние воспроизведения MediaPlayer
    private fun startPlayer() {
        interactor.start()
        view.setPlayIcon(pause)
        playerState = STATE_PLAYING
    }

    fun pausePlayer() {
        interactor.pause()
        view.setPlayIcon(play)
        playerState = STATE_PAUSED
    }

    fun preparePlayer(track: Track) {
        interactor.preparePlayer(track)
    }

    fun clearPlayer() {
        interactor.releasePlayer()
        mainThreadHandler.removeCallbacks(progressTime)
    }

    fun resetPlayer() {
        interactor.resetPlayer()
    }

    override fun setTimeInZero() {
        view.setProgressTime("00:00")
    }

    fun TimeFormatUtil():String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(interactor.getCurrentTime())
    }



    override fun setImagePlay() {
        view.setPlayIcon(play)
    }

    override fun setStatePrepared() {
        view.setPlayIcon(play)
        playerState = STATE_PREPARED
    }

    override fun removeHandlersCallbacks() {
        mainThreadHandler.removeCallbacks(progressTime)
    }


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 500L
    }
}