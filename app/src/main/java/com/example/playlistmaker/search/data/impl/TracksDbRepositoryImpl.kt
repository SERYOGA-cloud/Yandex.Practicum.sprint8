package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.db.AppDataBase
import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.api.TracksDbRepository
import com.example.playlistmaker.search.domain.converters.TrackConverter
import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksDbRepositoryImpl(val dataBase: AppDataBase) : TracksDbRepository {

    private val trackDao = dataBase.trackDao()

    override suspend fun addToFavorites(track: TrackEntity) {
        trackDao.insertTrack(track)
    }

    override suspend fun deleteFromFavorites(track: TrackEntity) {
        trackDao.deleteTrack(track)
    }

    override fun getFavorites(): Flow<List<Track>> {
        return trackDao.getTracks().map { entity ->
            TrackConverter.convertFromDbEntityList(entity)
        }
    }
}