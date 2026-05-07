package com.example.playlistmaker.player.ui.entity

import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.util.TRACK_PROGRESS_TIME_DEFAULT

data class PlayerScreenState(
    val currentPosition: String,
    val isFavorite: Boolean,
    val isPlayButtonShown: Boolean,
    val isBottomSheetVisible: Boolean,
    val playlists: List<Playlist>
) {

    companion object {
        val Default = PlayerScreenState(
            currentPosition = TRACK_PROGRESS_TIME_DEFAULT,
            isFavorite = false,
            isPlayButtonShown = true,
            isBottomSheetVisible = false,
            playlists = emptyList()
        )
    }
}