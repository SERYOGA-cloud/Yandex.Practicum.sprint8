package com.example.playlistmaker.mediateka.playlists.domain.api

import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean

    fun getAllPlaylists(): Flow<List<Playlist>>

    fun getTracksByPlaylistId(playlistId: Int): Flow<List<Track>>

    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int)
    fun getPlaylistById(playlistId: Int): Flow<Playlist>
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
}