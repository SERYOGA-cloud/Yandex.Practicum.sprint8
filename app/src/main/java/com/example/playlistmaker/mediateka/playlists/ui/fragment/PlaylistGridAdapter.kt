package com.example.playlistmaker.mediateka.playlists.ui.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ItemPlaylistGridBinding
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.util.ClickDebouncer

class PlaylistGridAdapter(
    private val onItemClicked: (playlist: Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistGridViewHolder>(), ClickDebouncer {

    override var isClickAllowed = true
    private val playlists = mutableListOf<Playlist>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistGridViewHolder {
        return PlaylistGridViewHolder(
            binding = ItemPlaylistGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistGridViewHolder, position: Int) {
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
        this.notifyDataSetChanged()
    }
}