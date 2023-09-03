package com.practicum.mymediaplayer.domain.interactor




import com.practicum.mymediaplayer.data.repository.TrackRepositoryImpl
import com.practicum.mymediaplayer.domain.models.Track
import com.practicum.mymediaplayer.domain.repository.PlayerInteractor
import com.practicum.mymediaplayer.domain.repository.PlayerModeListener
import com.practicum.mymediaplayer.domain.repository.TrackRepository


class Interactor(private val playerModeListener: PlayerModeListener) : PlayerInteractor,
    TrackRepository {

    private val playerInteractor = PlayerInteractorImpl(this)

    /*  fun transferTrackClicked(track: Track) {
           val trackClicked = this.saveTrack(track)
           //saveTrack(track)
       }
   */


    fun start() {
        playerInteractor.start()
    }
    fun pause() {
        playerInteractor.pause()
    }

    fun preparePlayer(track: Track) {
        playerInteractor.preparePlayer(track)
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
    }

    fun resetPlayer() {
        playerInteractor.resetPlayer()
    }
    fun getCurrentTime(): Int {
        return playerInteractor.getCurrentTime()
    }

    fun isMediaPlayerPlay(): Boolean {
        return playerInteractor.isMediaPlayerPlay()
    }
    fun onCompletionListener() {
        playerInteractor.onCompletionListener()
    }

    override fun setStatePrepared() {
        playerModeListener.setStatePrepared()
    }

    override fun removeHandlersCallbacks() {
        playerModeListener.removeHandlersCallbacks()
    }

    override fun setImagePlay() {
        playerModeListener.setImagePlay()
    }

    override fun setTimeInZero() {
        playerModeListener.setTimeInZero()
    }

    override fun getTrack(): Track {
        TODO("Not yet implemented")
    }

    override fun saveTrack(trackClicked: Track){
    val trackToSave=trackClicked
    //trackRepository.saveTrack(trackClicked)

    }


}