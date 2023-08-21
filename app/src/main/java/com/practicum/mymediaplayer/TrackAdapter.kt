package com.practicum.mymediaplayer


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class TrackAdapter(val listener: TrackSavedAdapter.TrackClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], listener)
    }

    override fun getItemCount(): Int = tracks.size

}
