package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.entity.Resource
import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String) : Flow<Resource<List<Track>>>
}