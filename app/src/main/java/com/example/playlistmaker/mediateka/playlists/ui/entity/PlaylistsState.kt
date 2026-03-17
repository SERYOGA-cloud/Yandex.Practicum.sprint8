package com.example.playlistmaker.mediateka.playlists.ui.entity

import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist

sealed interface PlaylistsState {

    data object Placeholder : PlaylistsState

    data class Content(val playlists: List<Playlist>) : PlaylistsState
}