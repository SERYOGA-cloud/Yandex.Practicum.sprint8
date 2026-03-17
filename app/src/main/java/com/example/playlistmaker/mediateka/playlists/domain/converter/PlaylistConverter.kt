package com.example.playlistmaker.mediateka.playlists.domain.converter

import androidx.core.net.toUri
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistEntity
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistWithTracks
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.search.domain.converters.TrackConverter.convertFromDbEntityList

object PlaylistConverter {

    fun toPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            coverUri = playlist.coverUri.toString()
        )
    }

    fun toPlaylist(playlistWithTracks: PlaylistWithTracks): Playlist {
        return Playlist(
            id = playlistWithTracks.playlist.id,
            tracks = convertFromDbEntityList(playlistWithTracks.tracks),
            title = playlistWithTracks.playlist.title,
            description = playlistWithTracks.playlist.description,
            coverUri = playlistWithTracks.playlist.coverUri?.toUri()
        )
    }
}