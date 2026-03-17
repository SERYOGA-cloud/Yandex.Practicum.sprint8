package com.example.playlistmaker.mediateka.playlists.domain.entity

import android.net.Uri
import com.example.playlistmaker.search.domain.entity.Track

data class Playlist(
    val id: Int = 0,
    val tracks: List<Track>?,
    val title: String,
    val description: String?,
    val coverUri: Uri?
) {
    fun getTracksCount(): Int = tracks?.size ?: 0
}