package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.util.Util.Companion.millisToMmSs

object TrackListFromDtoMapper {
    fun map(listDto: List<TrackDto>, favoritesIds: List<Int>): List<Track> {
        return listDto.map {
            Track(
                trackId = it.trackId,
                trackName = it.trackName,
                artistName = it.artistName,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                trackTimeConverted = millisToMmSs(it.trackTimeMillis),
                artworkUrl100 = it.artworkUrl100,
                previewUrl = it.previewUrl,
                isFavorite = favoritesIds.contains(it.trackId)
            )
        }
    }

    fun map(listDto: List<TrackDto>): List<Track> {
        return listDto.map {
            Track(
                trackId = it.trackId,
                trackName = it.trackName,
                artistName = it.artistName,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                trackTimeConverted = millisToMmSs(it.trackTimeMillis),
                artworkUrl100 = it.artworkUrl100,
                previewUrl = it.previewUrl,
                isFavorite = it.isFavorite
            )
        }
    }
}