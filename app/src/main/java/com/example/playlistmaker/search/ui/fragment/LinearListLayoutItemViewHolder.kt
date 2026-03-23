package com.example.playlistmaker.search.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemLinearListLayoutBinding
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.util.Util.Companion.dpToPx
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.util.Util.Companion.getRusNumeralTrackEnding

class LinearListLayoutItemViewHolder(private val binding: ItemLinearListLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        binding.apply {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = track.trackTimeConverted

            dotTextDivider.visibility = View.VISIBLE
            trackTime.visibility = View.VISIBLE
        }
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.album_placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(dpToPx(2f, itemView.context))
            )
            .into(binding.albumCover)
    }

    fun bind(playlist: Playlist) {
        binding.apply {
            val trackCount = playlist.getTracksCount()
            val totalTracksText = root.context.getString(
                R.string.tracks,
                trackCount,
                getRusNumeralTrackEnding(trackCount)
            )

            trackName.text = playlist.title
            artistName.text = totalTracksText

            dotTextDivider.visibility = View.GONE
            trackTime.visibility = View.GONE

            buttonToTrack.visibility = View.GONE
        }
        Glide.with(itemView)
            .load(playlist.coverUri)
            .placeholder(R.drawable.album_placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(dpToPx(2f, itemView.context))
            )
            .into(binding.albumCover)
    }

    companion object {
        fun from(parent: ViewGroup): LinearListLayoutItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemLinearListLayoutBinding.inflate(inflater, parent, false)
            return LinearListLayoutItemViewHolder(binding)
        }
    }
}