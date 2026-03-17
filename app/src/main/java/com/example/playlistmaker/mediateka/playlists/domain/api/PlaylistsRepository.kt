package com.example.playlistmaker.mediateka.playlists.domain.api

import com.example.playlistmaker.mediateka.favorites.db.entity.TrackEntity
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistEntity
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun createPlaylist(playlist: PlaylistEntity)

    suspend fun addTrackToPlaylist(playlistId: Int, track: TrackEntity): Boolean

    fun getAllPlaylists(): Flow<List<PlaylistWithTracks>>
}