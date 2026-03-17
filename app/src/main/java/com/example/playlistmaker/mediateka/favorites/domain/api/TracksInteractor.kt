package com.example.playlistmaker.mediateka.favorites.domain.api

import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {

    suspend fun addToDataBase(track: Track)

    suspend fun deleteFromFavorites(track: Track)

    fun getFavorites(): Flow<List<Track>>
}