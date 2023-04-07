package com.practicum.mymediaplayer

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackSavedAdapter() : RecyclerView.Adapter<TrackViewHolder>() {


    var trackSaved=ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        holder.bind(trackSaved.get(position))
    }

    override fun getItemCount(): Int = trackSaved.size



}
