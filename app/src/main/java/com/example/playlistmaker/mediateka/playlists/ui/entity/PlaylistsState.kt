package com.example.playlistmaker.mediateka.playlists.ui.entity

import com.example.playlistmaker.R
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist

sealed interface PlaylistsState {

    data class Placeholder(
        val textId: Int = R.string.you_havent_created_any_playlists,
        val imageId: Int = R.drawable.img_nothing_found
    ) : PlaylistsState

    data class Content(val playlists: List<Playlist>) : PlaylistsState
}