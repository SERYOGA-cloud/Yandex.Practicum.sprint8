package com.example.playlistmaker.mediateka.playlists.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.playlistmaker.mediateka.favorites.db.entity.TrackEntity

@Entity(
    tableName = "playlists_tracks",
    primaryKeys = ["playlistId", "trackId"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TrackEntity::class,
            parentColumns = ["trackId"],
            childColumns = ["trackId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("trackId")]
)
data class PlaylistTrackRelation(
    val playlistId: Int,
    val trackId: Int
)