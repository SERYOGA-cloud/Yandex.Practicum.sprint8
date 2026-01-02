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
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder.from(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val currentTrack = tracks[position]
        holder.bind(currentTrack)
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                onItemClicked.invoke(currentTrack)
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

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}