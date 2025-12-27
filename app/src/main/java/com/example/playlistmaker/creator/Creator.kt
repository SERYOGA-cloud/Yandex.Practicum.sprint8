package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.player.data.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker.settings.data.impl.ThemeRepositoryImpl
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.playlistmaker.player.domain.api.PlayerStateListener
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.settings.domain.api.ThemeRepository
import com.example.playlistmaker.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.settings.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.search.domain.impl.TrackSearchInteractorImpl

const val APP_PREFERENCES = "playlist_maker_preferences"

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        Creator.application = application
    }

    //Interactors
    //
    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTracksRepository())
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getHistoryRepository())
    }

    fun provideAudioPlayerInteractor(
        previewUrl: String?,
        onStateChangedListener: PlayerStateListener
    ): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getAudioPlayerRepository(previewUrl), onStateChangedListener)
    }

    fun provideThemeInteractor(): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository())
    }

    //Internal
    //
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(getSharedPreferences())
    }

    private fun getAudioPlayerRepository(previewUrl: String?): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl(previewUrl)
    }

    private fun getThemeRepository(): ThemeRepository {
        return ThemeRepositoryImpl(getSharedPreferences())
    }

    private fun getSharedPreferences(): SharedPreferences {
        return application.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
    }
}
