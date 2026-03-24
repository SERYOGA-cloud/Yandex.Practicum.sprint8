package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.favorites.ui.viewModel.FavoritesViewModel
import com.example.playlistmaker.mediateka.playlists.ui.viewModel.NewPlaylistViewModel
import com.example.playlistmaker.mediateka.playlists.ui.viewModel.PlaylistsViewModel
import com.example.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel
import com.example.playlistmaker.settings.ui.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
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

    viewModel<PlayerViewModel> { (url: String?) ->

        PlayerViewModel(
            playerInteractor = get { parametersOf(url) },
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

    viewModel<NewPlaylistViewModel> {
        NewPlaylistViewModel(
            playlistsInteractor = get(),
            storageInteractor = get(),
            stringResProvider = get()
        )
    }
}
