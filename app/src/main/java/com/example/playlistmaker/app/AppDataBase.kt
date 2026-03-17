package com.example.playlistmaker.app

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.mediateka.favorites.db.entity.TrackEntity
import com.example.playlistmaker.mediateka.playlists.db.dao.MediaDao
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistEntity
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistTrackRelation

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackRelation::class])
abstract class AppDataBase : RoomDatabase() {

    abstract fun mediaDao(): MediaDao
}