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
import java.text.SimpleDateFormat
import java.util.Locale


class TrackAdapter() : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks.get(position))
    }

    override fun getItemCount(): Int = tracks.size

}

class TrackViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.song_list_layout, parent, false)
    ) {

    var imageViewHolder: ImageView = itemView.findViewById(R.id.image)
    var trackNameView: TextView = itemView.findViewById(R.id.track_name)
    var artistNameView: TextView = itemView.findViewById(R.id.artist_name)
    var trackTimeView: TextView = itemView.findViewById(R.id.track_time)

    fun bind(model: Track) {

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .transform(
                RoundedCorners(
                    itemView.resources
                        .getDimensionPixelSize(R.dimen.setting_rounded_Corners)
                )
            )
            .placeholder(R.mipmap.ic_vector)
            .into(imageViewHolder)

        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)

    }
}
