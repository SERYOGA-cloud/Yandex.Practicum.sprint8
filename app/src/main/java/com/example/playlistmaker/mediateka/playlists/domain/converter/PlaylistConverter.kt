package com.example.playlistmaker.mediateka.playlists.domain.converter

import androidx.core.net.toUri
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistEntity
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistWithTracks
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.playlist.data.dto.PlaylistDto
import com.example.playlistmaker.search.domain.converters.TrackConverter

object PlaylistConverter {

    fun toPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            coverUri = playlist.coverUri.toString()
        )
    }

    fun convertToDomainList(entities: List<PlaylistWithTracks>): List<Playlist> {
        return entities.filter { it.playlist != null }
            .map { playlistWithTracks ->
                toPlaylist(playlistWithTracks)
            }
    }

    fun toPlaylist(playlistWithTracks: PlaylistWithTracks): Playlist {
        return Playlist(
            id = playlistWithTracks.playlist.id,
            tracks = playlistWithTracks.tracks.map { entity ->
                Track(
                    trackId = entity.trackId,
                    trackName = entity.trackName,
                    artistName = entity.artistName,
                    trackTimeMillis = entity.trackTimeMillis.toInt(),
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

    fun toPlaylistDto(playlist: Playlist): PlaylistDto {
        val tracksDto = playlist.tracks?.map { track ->
            TrackConverter.convertToDto(track)
        } ?: emptyList()

        return PlaylistDto(
            id = playlist.id,
            tracks = tracksDto,
            title = playlist.title,
            description = playlist.description,
            coverUri = playlist.coverUri
        )
    }
}