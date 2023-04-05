package com.practicum.mymediaplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*


class TrackViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.song_list_layout, parent, false)
    ), View.OnClickListener {

    //отслеживаем выбор трека в списке треков на экране для сохранения

    val songLayout: LinearLayout = itemView.findViewById(R.id.song_layout)

    init {
        songLayout.setOnClickListener(this)
    }

    private var imageViewHolder: ImageView = itemView.findViewById(R.id.image)
    private var trackNameView: TextView = itemView.findViewById(R.id.track_name)
    private var artistNameView: TextView = itemView.findViewById(R.id.artist_name)
    private var trackTimeView: TextView = itemView.findViewById(R.id.track_time)
    private var trackId: TextView = itemView.findViewById(R.id.track_id)
    var trackMill: Long = 0
    var diskCover: String = ""


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
        diskCover = model.artworkUrl100
        trackMill = model.trackTimeMillis
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackId.text = model.trackId
        /* преобразуем время трека из мс в формат ММ:СЕК */
        trackTimeView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)

    }

    //обработка нажатия на трек из списка
    override fun onClick(v: View?) {
        // Handle item click here
        trackSaved.reverse()
        val trackString = Track(
            "${trackId.text}", "${trackNameView.text}", "${artistNameView.text}",
            trackMill, diskCover
        )
        val trackNumber = trackString.trackId.toInt()
        removeDouble()
        val size = trackSaved.size
        for (i in 0 until size) {

            if (trackNumber == trackSaved[i].trackId.toInt())
                trackSaved.removeAt(i)
            break
        }
/*
        Toast.makeText(
            v?.context,
            "Track saved:  ${trackNameView.text} ${artistNameView.text} ",
            Toast.LENGTH_SHORT
        ).show()

 */
        limitSizeOfTrackSaved()
        trackSaved.add(trackString)
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
        if (trackSaved.size > maxSize) {
            // расчитываем сколько треков нужно удалить
            val numExcessObjects = trackSaved.size - maxSize

            // удаляем треки
            for (i in 0..numExcessObjects) {
                trackSaved.removeAt(i)
            }
        }
    }


}
