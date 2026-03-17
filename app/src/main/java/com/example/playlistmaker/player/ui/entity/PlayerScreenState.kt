package com.example.playlistmaker.player.ui.entity

import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.player.domain.entity.PlayerState

data class PlayerScreenState(
    val currentPosition: String,
    val playerState: PlayerState,
    val isFavorite: Boolean,
    val isPlayButtonShown: Boolean,
    val isBottomSheetVisible: Boolean,
    val playlists: List<Playlist>
) {

    companion object {
        val Default = PlayerScreenState(
            currentPosition = "00:00",
            playerState = PlayerState.DEFAULT,
            isFavorite = false,
            isPlayButtonShown = true,
            isBottomSheetVisible = false,
            playlists = emptyList()
        )
    }
}