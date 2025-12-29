package com.example.playlistmaker.di

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
import org.koin.core.parameter.parametersOf
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

    factory<AudioPlayerInteractor> { (url: String?) ->

        if (url != null) AudioPlayerInteractorImpl(
            repository = get { parametersOf(url) }
        ) else {
            FailedAudioPlayerInteractorImpl()
        }
    }
}
