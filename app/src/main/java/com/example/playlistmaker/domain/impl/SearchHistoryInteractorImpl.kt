package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.mapper.TrackListFromDtoMapper
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.mapper.TrackToDtoConverter

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {

    override fun saveTrackToHistory(track: Track) {
        val dto = TrackToDtoConverter.convert(track)
        repository.saveTrackToHistory(dto)
    }

    override fun getHistoryList(): MutableList<Track> {
        val dto = repository.getHistoryList()
        return TrackListFromDtoMapper.map(dto).toMutableList()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}