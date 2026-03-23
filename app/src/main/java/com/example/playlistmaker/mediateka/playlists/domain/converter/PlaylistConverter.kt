package com.example.playlistmaker.mediateka.playlists.domain.converter

import androidx.core.net.toUri
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistEntity
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistWithTracks
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.search.domain.entity.Track // Добавьте этот импорт

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
            // Заменяем вызов внешнего конвертера на прямое мапирование:
            tracks = playlistWithTracks.tracks.map { entity ->
                Track(
                    trackId = entity.trackId,
                    trackName = entity.trackName,
                    artistName = entity.artistName,
                    trackTimeConverted = entity.trackTimeConverted,
                    artworkUrl100 = entity.artworkUrl100,
                    collectionName = entity.collectionName ?: "",
                    releaseDate = entity.releaseDate,
                    primaryGenreName = entity.primaryGenreName ?: "",
                    country = entity.country,
                    previewUrl = entity.previewUrl ?: "",
                    isFavorite = false
                )
            },
            title = playlistWithTracks.playlist.title,
            description = playlistWithTracks.playlist.description,
            coverUri = playlistWithTracks.playlist.coverUri?.toUri()
        )
    }
}