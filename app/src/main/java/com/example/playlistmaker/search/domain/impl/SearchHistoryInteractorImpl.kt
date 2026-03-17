package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.data.mapper.TrackListFromDtoMapper
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.search.domain.converters.TrackConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {

    override fun saveTrackToHistory(track: Track) {
        val dto = TrackConverter.convertToDto(track)
        repository.saveTrackToHistory(dto)
    }

    override fun getHistoryList(): Flow<MutableList<Track>> {
        return repository.getHistoryList().map { listDto ->
            TrackListFromDtoMapper.map(listDto).toMutableList()
        }
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}