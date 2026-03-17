package com.example.playlistmaker.mediateka.playlists.ui.fragment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistGridBinding
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.util.ClickDebouncer
import com.example.playlistmaker.util.Util.Companion.dpToPx
import com.example.playlistmaker.util.Util.Companion.getRusNumeralTrackEnding

class PlaylistGridViewHolder(private val binding: ItemPlaylistGridBinding) :
    RecyclerView.ViewHolder(binding.root), ClickDebouncer {

    override var isClickAllowed = true

    fun bind(model: Playlist) {
        binding.apply {
            val trackCount = model.getTracksCount()
            val totalTracksText =
                root.context.getString(R.string.tracks, trackCount, getRusNumeralTrackEnding(trackCount))

            playlistTitle.text = model.title
            playlistTracksCount.text = totalTracksText
            Glide.with(itemView)
                .load(model.coverUri)
                .placeholder(R.drawable.album_placeholder)
                .transform(
                    CenterCrop(),
                    RoundedCorners(dpToPx(8f, itemView.context))
                )
                .into(binding.playlistCover)
        }
    }
}