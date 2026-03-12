package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {

    fun saveTrackToHistory(track: Track)

    fun getHistoryList(): Flow<MutableList<Track>>

    fun clearHistory()
}