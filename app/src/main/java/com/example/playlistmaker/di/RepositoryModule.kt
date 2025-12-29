package com.example.playlistmaker.di

import com.example.playlistmaker.player.data.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksRepository
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
        ExternalNavigatorImpl(
            context = androidContext()
        )
    }

    single<StringResourceProvider> {
        StringResourceProviderImpl(
            context = androidContext()
        )
    }

    single<TracksRepository> {
        TracksRepositoryImpl(
            networkClient = get()
        )
    }
    
    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            prefs = get(),
            gson = get()
        )
    }

    factory<AudioPlayerRepository> { (previewUrl: String) ->
        AudioPlayerRepositoryImpl(
            previewUrl = previewUrl
        )
    }
}
