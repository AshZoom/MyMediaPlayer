package com.practicum.mymediaplayer


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


//class TrackAdapter(private val clickListener: TrackClickListener) : RecyclerView.Adapter<TrackViewHolder>() {
class TrackAdapter() : RecyclerView.Adapter<TrackViewHolder>() {
    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks.get(position))

    }

    override fun getItemCount(): Int = tracks.size



}
