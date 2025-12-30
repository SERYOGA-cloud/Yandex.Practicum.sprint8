package com.example.playlistmaker.mediateka.ui.entity

sealed interface PlaylistsState {

    data object Placeholder: PlaylistsState
}