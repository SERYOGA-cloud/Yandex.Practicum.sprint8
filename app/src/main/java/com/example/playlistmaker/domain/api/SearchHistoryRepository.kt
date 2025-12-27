package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.TrackDto

interface SearchHistoryRepository {

    fun saveTrackToHistory(track: TrackDto)

    fun getHistoryList(): List<TrackDto>

    fun clearHistory()
}