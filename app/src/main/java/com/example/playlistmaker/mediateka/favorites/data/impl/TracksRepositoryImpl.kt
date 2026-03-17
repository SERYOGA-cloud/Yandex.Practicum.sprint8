package com.example.playlistmaker.mediateka.favorites.data.impl

import com.example.playlistmaker.app.AppDataBase
import com.example.playlistmaker.mediateka.favorites.db.entity.TrackEntity
import com.example.playlistmaker.mediateka.favorites.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.converters.TrackConverter
import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksRepositoryImpl(val dataBase: AppDataBase) : TracksRepository {

    private val mediaDao = dataBase.mediaDao()

    override suspend fun addToDataBase(track: TrackEntity) {
        mediaDao.insertTrack(track)
    }

    override suspend fun deleteFromFavorites(track: TrackEntity) {
        mediaDao.removeTrackFromFavorites(track)
    }

    override fun getFavorites(): Flow<List<Track>> {
        return mediaDao.getFavoriteTracks().map { entity ->
            TrackConverter.convertFromDbEntityList(entity)
        }
    }
}