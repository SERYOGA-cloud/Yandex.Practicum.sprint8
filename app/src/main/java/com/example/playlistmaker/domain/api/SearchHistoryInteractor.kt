package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.entity.Track

interface SearchHistoryInteractor {

    fun saveTrackToHistory(track: Track)

    fun getHistoryList(): MutableList<Track>

    fun clearHistory()
}