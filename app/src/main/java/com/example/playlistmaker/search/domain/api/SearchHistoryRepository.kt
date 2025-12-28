package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.data.dto.TrackDto

interface SearchHistoryRepository {

    fun saveTrackToHistory(track: TrackDto)

    fun getHistoryList(): List<TrackDto>

    fun clearHistory()
}