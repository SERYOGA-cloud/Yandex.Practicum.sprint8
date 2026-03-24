package com.example.playlistmaker.playlist.ui.entity

sealed class PlaylistScreenState {

    data object Empty : PlaylistScreenState()

    data object ContentBottomSheet : PlaylistScreenState()

    data object MenuBottomSheet : PlaylistScreenState()

    data object Sharing : PlaylistScreenState()
}