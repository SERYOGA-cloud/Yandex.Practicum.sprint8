package com.example.playlistmaker.data.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.google.gson.Gson

const val SEARCH_HISTORY = "search_history"

class SearchHistoryRepositoryImpl(private val prefs: SharedPreferences) :
    SearchHistoryRepository {

    private val gson = Gson()
    private val tracksHistory = mutableListOf<TrackDto>()

    init {
        loadHistoryFromPrefs()
    }

    override fun saveTrackToHistory(track: TrackDto) {
        removeTrackIfExists(track)
        tracksHistory.add(0, track)
        if (tracksHistory.size > MAX_HISTORY_SIZE) {
            tracksHistory.removeAt(tracksHistory.lastIndex)
        }
        saveHistoryToPrefs()
    }

    override fun getHistoryList(): MutableList<TrackDto> {
        return tracksHistory
    }

    override fun clearHistory() {
        tracksHistory.clear()
        saveHistoryToPrefs()
    }

    private fun loadHistoryFromPrefs() {
        val json = prefs.getString(SEARCH_HISTORY, null)
        val arrayFromPrefs = gson.fromJson(json, Array<TrackDto>::class.java) ?: emptyArray()
        tracksHistory.addAll(arrayFromPrefs)
    }

    private fun removeTrackIfExists(track: TrackDto) {
        val iterator = tracksHistory.iterator()
        while (iterator.hasNext()) {
            val currentTrack = iterator.next()
            if (currentTrack.trackId == track.trackId) {
                iterator.remove()
            }
        }
    }

    private fun saveHistoryToPrefs() {
        val json = gson.toJson(tracksHistory.take(MAX_HISTORY_SIZE)).toString()
        prefs.edit().putString(SEARCH_HISTORY, json).apply()
    }

    companion object {
        const val MAX_HISTORY_SIZE = 10
    }
}