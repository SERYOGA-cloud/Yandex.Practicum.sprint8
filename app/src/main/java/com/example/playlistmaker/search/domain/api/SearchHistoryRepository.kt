package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    fun saveTrackToHistory(track: TrackDto)

    fun getHistoryList(): Flow<List<TrackDto>>

    fun clearHistory()
}