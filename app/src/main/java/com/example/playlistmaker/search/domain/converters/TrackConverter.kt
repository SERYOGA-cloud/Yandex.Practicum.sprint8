package com.example.playlistmaker.search.domain.converters

import com.example.playlistmaker.mediateka.favorites.db.entity.TrackEntity
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.util.Util.Companion.mmSsToMillis

object TrackConverter {

    fun convertToDto(track: Track): TrackDto {
        return TrackDto(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            trackTimeMillis = mmSsToMillis(track.trackTimeConverted),
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl,
            isFavorite = track.isFavorite
        )
    }

    fun convertToDbEntity(track: Track): TrackEntity {
        return TrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            trackTimeConverted = track.trackTimeConverted,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl,
            isFavorite = if (track.isFavorite) 1 else 0
        )
    }

    private fun convertFromDbEntity(trackEntity: TrackEntity): Track {
        return Track(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            collectionName = trackEntity.collectionName,
            releaseDate = trackEntity.releaseDate,
            primaryGenreName = trackEntity.primaryGenreName,
            country = trackEntity.country,
            trackTimeConverted = trackEntity.trackTimeConverted,
            artworkUrl100 = trackEntity.artworkUrl100,
            previewUrl = trackEntity.previewUrl,
            isFavorite = trackEntity.isFavorite == 1
        )
    }

    fun convertFromDbEntityList(dbList: List<TrackEntity>): List<Track> {
        return dbList.map { trackEntity -> convertFromDbEntity(trackEntity) }
    }
}