package com.example.playlistmaker.mediateka.playlists.domain.entity

import android.net.Uri
import android.os.Parcelable
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.util.Util
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Int = 0,
    val tracks: List<Track>?,
    val title: String,
    val description: String?,
    val coverUri: Uri?
) : Parcelable {
    fun getTracksCount(): Int = tracks?.size ?: 0

    fun getTotalTracksDuration(): Int {
        if (tracks == null) {
            return 0
        } else {
            var totalTimeMillis = 0
            tracks.forEach { track ->
                totalTimeMillis += track.trackTimeMillis
            }
            return Util.millisToMinutes(totalTimeMillis)
        }
    }
}