package com.example.playlistmaker.playlist.data.dto

import android.net.Uri
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.util.Util.Companion.millisToMinutes

class PlaylistDto(
    val id: Int,
    val tracks: List<TrackDto>?,
    val title: String,
    val description: String?,
    val coverUri: Uri?
) {
    fun getTracksCount(): Int = tracks?.size ?: 0

    fun getTotalTracksDuration(): Int {
        if (tracks == null) {
            return 0
        } else {
            var totalTimeMillis = 0
            tracks.forEach { track ->
                totalTimeMillis += track.trackTimeMillis
            }
            return millisToMinutes(totalTimeMillis)
        }
    }
}