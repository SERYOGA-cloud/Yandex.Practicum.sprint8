package com.example.playlistmaker.search.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackSearchItemBinding
import com.example.playlistmaker.util.Util.Companion.dpToPx
import com.example.playlistmaker.search.domain.entity.Track

class TrackViewHolder(private val binding: TrackSearchItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track) {
        binding.trackName.text = model.trackName
        binding.artistName.text = model.artistName
        binding.trackTime.text = model.trackTimeConverted
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.album_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(binding.albumCover)
    }

    companion object {
        fun from(parent: ViewGroup): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TrackSearchItemBinding.inflate(inflater, parent, false)
            return TrackViewHolder(binding)
        }
    }
}
