package com.example.playlistmaker.mediateka.playlists.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.playlistmaker.mediateka.favorites.db.entity.TrackEntity
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistEntity
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistTrackRelation
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {

    // Работа с треками

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT COUNT(*) FROM playlists_tracks WHERE trackId = :trackId")
    suspend fun getPlaylistCountForTrack(trackId: Int): Int

    @Transaction
    suspend fun removeTrackFromFavorites(track: TrackEntity) {
        val updatedTrack = track.copy(isFavorite = 0)
        updateTrack(updatedTrack)

// Проверяем, есть ли трек в каких-либо плейлистах
        val playlistCount = getPlaylistCountForTrack(track.trackId)

        // Если трек не в избранном и не в плейлистах - удаляем из таблицы
        if (playlistCount == 0) {
            deleteTrack(updatedTrack)
        }
    }

    @Query("SELECT * FROM track_table WHERE isFavorite= 1 ORDER BY createdAtTime DESC")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM track_table")
    fun getAllTrackIds(): Flow<List<Int>>

    @Query("SELECT * FROM track_table WHERE trackId =:trackId")
    suspend fun getTrackById(trackId: Int): TrackEntity?

    // Работа с плейлистами
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Transaction
    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): Flow<List<PlaylistWithTracks>>

    // Треки - плейлисты

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaylistTrackRelation(relation: PlaylistTrackRelation)

    @Query("SELECT COUNT(*) FROM playlists_tracks WHERE playlistId =:playlistId AND trackId =:trackId")
    suspend fun isTrackInPlaylist(playlistId: Int, trackId: Int): Int

    @Transaction
    suspend fun addTrackToPlaylist(playlistId: Int, track: TrackEntity): Boolean {
        val isInPlaylist = isTrackInPlaylist(playlistId, track.trackId) > 0
        if (isInPlaylist) {
            return false
        }

        val existingTrack = getTrackById(track.trackId)
        if (existingTrack == null) {
            insertTrack(track)
        }

        insertPlaylistTrackRelation(PlaylistTrackRelation(playlistId, track.trackId))
        return true
    }
}