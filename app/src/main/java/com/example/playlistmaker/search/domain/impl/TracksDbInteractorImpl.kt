package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksDbInteractor
import com.example.playlistmaker.search.domain.api.TracksDbRepository
import com.example.playlistmaker.search.domain.converters.TrackConverter.convertToDbEntity
import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

class TracksDbInteractorImpl(val repository: TracksDbRepository) : TracksDbInteractor {
    override suspend fun addToFavorites(track: Track) {
        repository.addToFavorites(convertToDbEntity(track))
    }

    override suspend fun deleteFromFavorites(track: Track) {
        repository.deleteFromFavorites(convertToDbEntity(track))
    }

    override fun getFavorites(): Flow<List<Track>> {
        return repository.getFavorites()
    }
}