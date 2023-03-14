package com.practicum.mymediaplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.module.LibraryGlideModule


class TrackAdapter(private var tracks: List<Track>) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.song_list_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageViewHolder: ImageView
    private val trackNameView: TextView
    private val artistNameView: TextView
    private val trackTimeView: TextView

    init {
        imageViewHolder = itemView.findViewById(R.id.image)
        trackNameView = itemView.findViewById(R.id.track_name)
        artistNameView = itemView.findViewById(R.id.artist_name)
        trackTimeView = itemView.findViewById(R.id.track_time)
    }

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .transform(
                RoundedCorners(itemView.resources
                .getDimensionPixelSize(R.dimen.setting_rounded_Corners)))
            .placeholder(R.mipmap.ic_vector)
            .into(imageViewHolder)

    }
}
