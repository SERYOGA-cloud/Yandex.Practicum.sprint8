package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.TracksDbRepositoryImpl
import com.example.playlistmaker.search.data.impl.TrackSearchRepositoryImpl
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksDbRepository
import com.example.playlistmaker.search.domain.api.TrackSearchRepository
import com.example.playlistmaker.settings.data.impl.ThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.api.ThemeRepository
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.impl.StringResourceProviderImpl
import com.example.playlistmaker.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.StringResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<ThemeRepository> {
        ThemeRepositoryImpl(
            prefs = get(),
            context = androidContext()
        )
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = androidContext()
        )
    }

    single<StringResourceProvider> {
        StringResourceProviderImpl(context = androidContext()
        )
    }

    single<TrackSearchRepository> {
        TrackSearchRepositoryImpl(
            dataBase = get(),
            networkClient = get()
        )
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            prefs = get(),
            gson = get(),
            dataBase = get()
        )
    }

    // з DIсоздаём MediaPlayer чере
    factory { MediaPlayer() }

    // ОДНА фабрика репозитория и она передаёт player
    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl()
    }

    factory<TracksDbRepository> {
        TracksDbRepositoryImpl(
            dataBase = get()
        )
    }
}