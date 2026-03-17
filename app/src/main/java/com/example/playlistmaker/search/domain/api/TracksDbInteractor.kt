package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface TracksDbInteractor {

    suspend fun addToFavorites(track: Track)

    suspend fun deleteFromFavorites(track: Track)

    fun getFavorites(): Flow<List<Track>>
}