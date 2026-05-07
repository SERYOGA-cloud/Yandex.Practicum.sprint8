package com.example.playlistmaker.mediateka.playlists.data.impl

import com.example.playlistmaker.app.AppDataBase
import com.example.playlistmaker.mediateka.favorites.db.entity.TrackEntity
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistEntity
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistWithTracks
import com.example.playlistmaker.mediateka.playlists.domain.api.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(dataBase: AppDataBase) : PlaylistsRepository {
    private val mediaDao = dataBase.mediaDao()

    override suspend fun createPlaylist(playlist: PlaylistEntity) {
        mediaDao.insertPlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlistId: Int, track: TrackEntity): Boolean {
        val isSuccess: Boolean = mediaDao.addTrackToPlaylist(playlistId, track)
        return isSuccess
    }

    override fun getAllPlaylists(): Flow<List<PlaylistWithTracks>> {
        return mediaDao.getAllPlaylists()
    }

    override fun getTracksByPlaylistId(playlistId: Int): Flow<List<TrackEntity>?> {
        return mediaDao.getAllTracksByPlaylistId(playlistId).map { list ->
            list?.map { playlistTrack ->
                TrackEntity(
                    trackId = playlistTrack.trackId,
                    trackName = playlistTrack.trackName,
                    artistName = playlistTrack.artistName,
                    collectionName = playlistTrack.collectionName,
                    releaseDate = playlistTrack.releaseDate,
                    primaryGenreName = playlistTrack.primaryGenreName,
                    country = playlistTrack.country,
                    trackTimeMillis = playlistTrack.trackTimeMillis.toInt(),
                    trackTimeConverted = playlistTrack.trackTimeConverted,
                    artworkUrl100 = playlistTrack.artworkUrl100,
                    previewUrl = playlistTrack.previewUrl,
                    isFavorite = 0
                )
            }
        }
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        mediaDao.removeTrackFromPlaylist(playlistId, trackId)
    }

    override fun getPlaylistById(playlistId: Int): Flow<PlaylistWithTracks> {
        return mediaDao.getPlaylistById(playlistId)
    }

    override suspend fun deletePlaylist(playlist: PlaylistEntity) {
        mediaDao.deletePlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: PlaylistEntity) {
        mediaDao.updatePlaylist(playlist)
    }
}