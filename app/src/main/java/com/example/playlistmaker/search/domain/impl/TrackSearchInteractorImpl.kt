package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.search.domain.api.TrackSearchRepository
import com.example.playlistmaker.search.domain.entity.Resource
import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

class TrackSearchInteractorImpl(private val repository: TrackSearchRepository) : TrackSearchInteractor {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> {
        return repository.searchTracks(expression)
    }
}