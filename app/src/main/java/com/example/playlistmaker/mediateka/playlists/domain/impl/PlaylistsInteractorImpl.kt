package com.example.playlistmaker.mediateka.playlists.domain.impl

import com.example.playlistmaker.mediateka.playlists.domain.api.PlaylistsInteractor
import com.example.playlistmaker.mediateka.playlists.domain.api.PlaylistsRepository
import com.example.playlistmaker.mediateka.playlists.domain.converter.PlaylistConverter.convertToDomainList
import com.example.playlistmaker.mediateka.playlists.domain.converter.PlaylistConverter.toPlaylist
import com.example.playlistmaker.mediateka.playlists.domain.converter.PlaylistConverter.toPlaylistEntity
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.search.domain.converters.TrackConverter
import com.example.playlistmaker.search.domain.converters.TrackConverter.convertFromDbEntityList
import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository) :
    PlaylistsInteractor {
    override suspend fun createPlaylist(playlist: Playlist) {
        repository.createPlaylist(toPlaylistEntity(playlist))
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean {
        return repository.addTrackToPlaylist(
            playlist.id,
            TrackConverter.convertToDbEntity(track)
        )
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists().map {
            convertToDomainList(it)
        }
    }

    override fun getTracksByPlaylistId(playlistId: Int): Flow<List<Track>> {
        return repository.getTracksByPlaylistId(playlistId).map { entityList ->
            convertFromDbEntityList(entityList)
        }
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        repository.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override fun getPlaylistById(playlistId: Int): Flow<Playlist> {
        return repository.getPlaylistById(playlistId)
            .filterNotNull()
            .map { entity ->
                toPlaylist(entity)
            }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(toPlaylistEntity(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(toPlaylistEntity(playlist))
    }
}
