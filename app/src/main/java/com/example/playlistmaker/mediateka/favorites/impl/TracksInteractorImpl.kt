package com.example.playlistmaker.mediateka.favorites.domain.impl

import com.example.playlistmaker.mediateka.favorites.domain.api.TracksInteractor
import com.example.playlistmaker.mediateka.favorites.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.converters.TrackConverter.convertToDbEntity
import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(val repository: TracksRepository) : TracksInteractor {
    override suspend fun addToDataBase(track: Track) {
        repository.addToDataBase(convertToDbEntity(track))
    }

    override suspend fun deleteFromFavorites(track: Track) {
        repository.deleteFromFavorites(convertToDbEntity(track))
    }

    override fun getFavorites(): Flow<List<Track>> {
        return repository.getFavorites()
    }
}