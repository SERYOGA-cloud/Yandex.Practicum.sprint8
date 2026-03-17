package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface TracksDbRepository {

    suspend fun addToFavorites(track: TrackEntity)

    suspend fun deleteFromFavorites(track: TrackEntity)

    fun getFavorites(): Flow<List<Track>>
}