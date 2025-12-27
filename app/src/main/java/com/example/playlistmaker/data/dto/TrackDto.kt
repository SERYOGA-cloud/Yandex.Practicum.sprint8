package com.example.playlistmaker.data.dto

data class TrackDto(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val previewUrl: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TrackDto) return false

        return this.trackId == other.trackId
    }

    override fun hashCode(): Int {
        return trackId
    }
}