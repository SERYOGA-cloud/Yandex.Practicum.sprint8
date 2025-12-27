package com.example.playlistmaker.presentation.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.util.Util.Companion.dpToPx
import com.example.playlistmaker.domain.entity.Track

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val albumCover: ImageView = itemView.findViewById(R.id.album_cover)
    private var trackId: Int = -1

    fun bind(model: Track) {
        trackId = model.trackId
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTimeConverted
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.album_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(albumCover)
    }

}