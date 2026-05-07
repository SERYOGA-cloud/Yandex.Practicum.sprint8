package com.example.playlistmaker.sharing.domain.api

import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist

interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    fun sharePlaylist(playlist: Playlist)
}
