package com.example.playlistmaker.search.ui.fragment

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.util.ClickDebouncer

class TrackAdapter(
    val onItemClicked: (track: Track) -> Unit,
    val onItemLongClicked: (track: Track) -> Unit
) : RecyclerView.Adapter<LinearListLayoutItemViewHolder>(), ClickDebouncer {

    private val tracks = mutableListOf<Track>()
    override var isClickAllowed = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinearListLayoutItemViewHolder =
        LinearListLayoutItemViewHolder.from(parent)

    override fun onBindViewHolder(holder: LinearListLayoutItemViewHolder, position: Int) {
        val currentTrack = tracks[position]
        holder.bind(currentTrack)
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                onItemClicked.invoke(currentTrack)
            }
        }
        holder.itemView.setOnLongClickListener {
            if (clickDebounce()) {
                onItemLongClicked.invoke(currentTrack)
                true
            } else {
                false
            }
        }
    }

    override fun getItemCount(): Int = tracks.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateTrackList(newList: List<Track>) {
        tracks.clear()
        tracks.addAll(newList)
        this.notifyDataSetChanged()
    }
}