package com.example.playlistmaker.domain.mapper

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.util.Util.Companion.mmSsToMillis

object TrackToDtoConverter {

    fun convert(track: Track): TrackDto {
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
            previewUrl = track.previewUrl
        )
    }
}