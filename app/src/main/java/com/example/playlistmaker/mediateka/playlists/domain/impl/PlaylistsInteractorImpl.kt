package com.example.playlistmaker.mediateka.playlists.domain.impl

import com.example.playlistmaker.mediateka.playlists.domain.api.PlaylistsInteractor
import com.example.playlistmaker.mediateka.playlists.domain.api.PlaylistsRepository
import com.example.playlistmaker.mediateka.playlists.domain.converter.PlaylistConverter
import com.example.playlistmaker.mediateka.playlists.domain.converter.PlaylistConverter.toPlaylistEntity
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.search.domain.converters.TrackConverter
import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow
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
        return repository.getAllPlaylists().map { playlistEntityList ->
            playlistEntityList.map { entity ->
                PlaylistConverter.toPlaylist(entity)
            }
        }
    }
}