package com.example.playlistmaker.mediateka.favorites.domain.api

import com.example.playlistmaker.mediateka.favorites.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {

    suspend fun addToFavorites(track: TrackEntity)

    suspend fun deleteFromFavorites(track: TrackEntity)

    fun getFavorites(): Flow<List<Track>>
}