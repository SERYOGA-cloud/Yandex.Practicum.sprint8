package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.favorites.ui.viewModel.FavoritesViewModel
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.mediateka.playlists.ui.viewModel.NewPlaylistViewModel
import com.example.playlistmaker.mediateka.playlists.ui.viewModel.PlaylistsViewModel
import com.example.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.example.playlistmaker.playlist.ui.viewModel.PlaylistViewModel
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel
import com.example.playlistmaker.settings.ui.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            sharingInteractor = get(),
            themeInteractor = get()
        )
    }

    viewModel<SearchViewModel> {
        SearchViewModel(
            trackSearchInteractor = get(),
            searchHistoryInteractor = get()
        )
    }

    viewModel<PlayerViewModel> {
        PlayerViewModel(
            tracksInteractor = get(),
            playlistsInteractor = get(),
            stringResourceProvider = get()
        )
    }

    viewModel<FavoritesViewModel> {
        FavoritesViewModel(
            dataBaseInteractor = get()
        )
    }

    viewModel<PlaylistsViewModel> {
        PlaylistsViewModel(
            playlistsInteractor = get()
        )
    }

    viewModel<NewPlaylistViewModel> { (existingPlaylist: Playlist?) ->
        NewPlaylistViewModel(
            playlistsInteractor = get(),
            storageInteractor = get(),
            stringResProvider = get(),
            existingPlaylist = existingPlaylist
        )
    }

    viewModel<PlaylistViewModel> { (playlist: Playlist) ->
        PlaylistViewModel(
            playlistsInteractor = get(),
            sharingInteractor = get(),
            stringResourceProvider = get(),
            initialPlaylist = playlist
        )
    }
}