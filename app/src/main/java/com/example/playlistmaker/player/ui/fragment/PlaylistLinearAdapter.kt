package com.example.playlistmaker.player.ui.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ItemLinearListLayoutBinding
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.search.ui.fragment.LinearListLayoutItemViewHolder
import com.example.playlistmaker.util.ClickDebouncer

class PlaylistLinearAdapter(
    private val onItemClicked: (playlist: Playlist) -> Unit
) : RecyclerView.Adapter<LinearListLayoutItemViewHolder>(), ClickDebouncer {

    override var isClickAllowed = true
    private val playlists = mutableListOf<Playlist>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LinearListLayoutItemViewHolder {
        return LinearListLayoutItemViewHolder(
            binding = ItemLinearListLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: LinearListLayoutItemViewHolder, position: Int) {
        val currentPlaylist = playlists[position]
        holder.bind(currentPlaylist)
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                onItemClicked.invoke(currentPlaylist)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePlaylists(newList: List<Playlist>) {
        playlists.clear()
        playlists.addAll(newList)
        notifyDataSetChanged()
    }
}