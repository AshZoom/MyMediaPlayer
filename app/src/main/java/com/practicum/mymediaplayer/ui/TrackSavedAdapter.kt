package com.practicum.mymediaplayer.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.mymediaplayer.domain.models.Track

class TrackSavedAdapter(val listener: TrackClickListener) : RecyclerView.Adapter<TrackViewHolder>() {


    var trackSaved=ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        holder.bind(trackSaved[position], listener)
    }

    override fun getItemCount(): Int = trackSaved.size

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }

}
