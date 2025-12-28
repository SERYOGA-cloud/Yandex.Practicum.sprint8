package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.entity.Track

interface SearchHistoryInteractor {

    fun saveTrackToHistory(track: Track)

    fun getHistoryList(): MutableList<Track>

    fun clearHistory()
}