package com.example.playlistmaker.di

import com.example.playlistmaker.mediateka.favorites.domain.api.TracksInteractor
import com.example.playlistmaker.mediateka.favorites.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.mediateka.playlists.domain.api.PlaylistsInteractor
import com.example.playlistmaker.mediateka.playlists.domain.api.StorageInteractor
import com.example.playlistmaker.mediateka.playlists.domain.impl.PlaylistsInteractorImpl
import com.example.playlistmaker.mediateka.playlists.domain.impl.StorageInteractorImpl
import com.example.playlistmaker.mediateka.playlists.domain.api.StorageRepository
import com.example.playlistmaker.mediateka.playlists.domain.api.PlaylistsRepository
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.player.domain.impl.FailedAudioPlayerInteractorImpl
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TrackSearchInteractorImpl
import com.example.playlistmaker.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<ThemeInteractor> {
        ThemeInteractorImpl(
            repository = get()
        )
    }

    single<SharingInteractor> {
        SharingInteractorImpl(
            externalNavigator = get(),
            stringResourceProvider = get()
        )
    }

    single<TrackSearchInteractor> {
        TrackSearchInteractorImpl(
            repository = get()
        )
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(
            repository = get()
        )
    }

    factory<AudioPlayerInteractor> { params ->
        val url: String? = params.getOrNull()
        if (!url.isNullOrBlank()) {
            AudioPlayerInteractorImpl(
                repository = get()
            )
        } else {
            FailedAudioPlayerInteractorImpl()
        }
    }

    single<TracksInteractor> {
        TracksInteractorImpl(
            repository = get()
        )
    }

    factory<PlaylistsInteractor> {
        PlaylistsInteractorImpl(
            repository = get<PlaylistsRepository>()
        )
    }

    factory<StorageInteractor> {
        StorageInteractorImpl(
            repository = get<StorageRepository>()
        )
    }
}