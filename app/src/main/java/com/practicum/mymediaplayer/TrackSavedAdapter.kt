package com.practicum.mymediaplayer

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackSavedAdapter(private val clickListener: TrackClickListener) : RecyclerView.Adapter<TrackViewHolder>() {


    var trackSaved=ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackSaved.get(position))
        holder.itemView.setOnClickListener { clickListener.onTrackClick(trackSaved.get(position)) }

    }

    override fun getItemCount(): Int = trackSaved.size

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }

}
