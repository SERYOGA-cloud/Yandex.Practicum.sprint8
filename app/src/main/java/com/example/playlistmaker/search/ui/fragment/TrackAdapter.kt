package com.example.playlistmaker.search.ui.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.domain.entity.Track

class TrackAdapter(
    private val onItemClicked: (track: Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    private val tracks = mutableListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder.from(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val currentTrack = tracks[position]
        holder.bind(currentTrack)
        holder.itemView.setOnClickListener {
            onItemClicked.invoke(currentTrack)
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