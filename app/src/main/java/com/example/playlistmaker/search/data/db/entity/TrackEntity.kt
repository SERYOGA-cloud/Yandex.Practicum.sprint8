package com.example.playlistmaker.search.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String?,
    val country: String,
    val trackTimeConverted: String,
    val artworkUrl100: String,
    val previewUrl: String?,
    val isFavorite: Boolean
)