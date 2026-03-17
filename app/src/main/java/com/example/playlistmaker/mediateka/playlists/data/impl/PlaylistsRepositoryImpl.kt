package com.example.playlistmaker.mediateka.playlists.data.impl

import com.example.playlistmaker.app.AppDataBase
import com.example.playlistmaker.mediateka.favorites.db.entity.TrackEntity
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistEntity
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistWithTracks
import com.example.playlistmaker.mediateka.playlists.domain.api.PlaylistsRepository
import kotlinx.coroutines.flow.Flow

class PlaylistsRepositoryImpl(dataBase: AppDataBase) : PlaylistsRepository {
    private val playlistDao = dataBase.mediaDao()

    override suspend fun createPlaylist(playlist: PlaylistEntity) {
        playlistDao.insertPlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlistId: Int, track: TrackEntity): Boolean {
        val isSuccess: Boolean = playlistDao.addTrackToPlaylist(playlistId, track)
        return isSuccess
    }

    override fun getAllPlaylists(): Flow<List<PlaylistWithTracks>> {
        return playlistDao.getAllPlaylists()
    }
}