package com.example.playlistmaker.util

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.data.impl.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.impl.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.impl.ThemeRepositoryImpl
import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.AudioPlayerRepository
import com.example.playlistmaker.domain.api.PlayerStateListener
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.ThemeRepository
import com.example.playlistmaker.domain.api.TrackSearchInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.impl.TrackSearchInteractorImpl

const val APP_PREFERENCES = "playlist_maker_preferences"

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
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
