package com.practicum.mymediaplayer.ui

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.mymediaplayer.R
import com.practicum.mymediaplayer.domain.models.Track
import com.practicum.mymediaplayer.trackSaved
import java.text.SimpleDateFormat
import java.util.Locale


lateinit var trackString: Track

class TrackViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.song_list_layout, parent, false)
    ) {

    lateinit var primaryGenreName: String
    lateinit var country: String
    lateinit var releaseDate: String
    lateinit var collectionName: String
    lateinit var diskCover: String
    lateinit var previewUrl: String

    private var imageViewHolder: ImageView = itemView.findViewById(R.id.image)
    private var trackNameView: TextView = itemView.findViewById(R.id.track_name)
    private var artistNameView: TextView = itemView.findViewById(R.id.artist_name)
    private var trackTimeView: TextView = itemView.findViewById(R.id.track_time)
    private var trackId: TextView = itemView.findViewById(R.id.track_id)
    var trackMill: Long = 0

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    fun bind(model: Track, listener: TrackSavedAdapter.TrackClickListener) {
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
        diskCover = model.artworkUrl100
        trackMill = model.trackTimeMillis
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackId.text = model.trackId
        primaryGenreName = model.primaryGenreName
        country = model.country
        releaseDate = model.releaseDate
        collectionName = model.collectionName
        previewUrl = model.previewUrl
        /* преобразуем время трека из мс в формат ММ:СЕК */
        trackTimeView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)

        itemView.setOnClickListener {
            handleItemClick(it)
            listener.onTrackClick(model)
        }
    }

    //обработка списка  при нажатии на трек из списка треков
    private fun handleItemClick(v: View?) {
        trackSaved.reverse()
        trackString = Track(
            "${trackId.text}",
            "${trackNameView.text}",
            "${artistNameView.text}",
            trackMill,
            diskCover,
            collectionName,
            releaseDate,
            primaryGenreName,
            previewUrl,
            country
        )
        val trackNumber = trackString.trackId.toInt()
        trackSaved.removeIf { it.trackId.toInt() == trackNumber }
        limitSizeOfTrackSaved()
        trackSaved.add(trackString)
        //удаляем из списка выбранных треков трек с одинаковым trackId
        removeDouble()
        trackSaved.reverse()
    }

    //удаляем дублирующиеся треки из спиcка сохраненных
    fun removeDouble() {
        //удаляем одинаковые треки из списка сохраненных треков
        val distinctList = trackSaved.distinct()
        trackSaved.clear()
        //формируем новый список сохраненных треков
        trackSaved.addAll(distinctList)
    }

    //ограничение количества сохраненных треков (10)
    fun limitSizeOfTrackSaved() {
        val maxSize = 10
        if (trackSaved.size >= maxSize) {
            // расчитываем сколько треков нужно удалить
            val numExcessObjects = trackSaved.size - maxSize
            // удаляем треки
            for (i in 0..numExcessObjects) {
                trackSaved.removeAt(i)
            }
        }
    }

}
