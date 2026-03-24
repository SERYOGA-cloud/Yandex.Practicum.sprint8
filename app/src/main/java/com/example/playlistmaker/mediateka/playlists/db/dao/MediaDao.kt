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
import com.example.playlistmaker.mediateka.playlists.db.entity.PlaylistTrackEntity

@Dao
interface MediaDao {

    // Работа с треками

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Transaction
    suspend fun addTrackToFavorites(track: TrackEntity) {
        val existingTrack = getTrackById(track.trackId)
        if (existingTrack == null) {
            insertTrack(track)
        } else {
            updateTrack(track.copy(isFavorite = 1))
        }
    }

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

    @Query(
        """
        SELECT t.* 
FROM playlists_tracks pt 
JOIN track_table t 
ON pt.trackId = t.trackId 
WHERE pt.playlistId = :playlistId"""
    )
    fun getAllTracksByPlaylistId(playlistId: Int): Flow<List<TrackEntity>?>

    // Работа с плейлистами
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Transaction
    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): Flow<List<PlaylistWithTracks>>

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Int): Flow<PlaylistWithTracks>

    // Треки - плейлисты

    @Query("DELETE FROM playlists_tracks WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun deletePlaylistTrackRelation(playlistId: Int, trackId: Int)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaylistTrackRelation(relation: PlaylistTrackRelation)

    @Query("SELECT COUNT(*) FROM playlists_tracks WHERE playlistId =:playlistId AND trackId =:trackId")
    suspend fun isTrackInPlaylist(playlistId: Int, trackId: Int): Int

    @Query("SELECT * FROM playlist_track_table WHERE trackId = :trackId")
    suspend fun getPlaylistTrack(trackId: Int): PlaylistTrackEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrack(track: PlaylistTrackEntity)

    @Transaction
    suspend fun addTrackToPlaylist(playlistId: Int, track: TrackEntity): Boolean {
        val isInPlaylist = isTrackInPlaylist(playlistId, track.trackId) > 0
        if (isInPlaylist) {
            return false
        }

        val existingTrack = getPlaylistTrack(track.trackId)
        if (existingTrack == null) {
            insertPlaylistTrack(
                PlaylistTrackEntity(
                    trackId = track.trackId,
                    trackName = track.trackName,
                    artistName = track.artistName,
                    collectionName = track.collectionName,
                    releaseDate = track.releaseDate,
                    primaryGenreName = track.primaryGenreName,
                    country = track.country,
                    trackTimeConverted = track.trackTimeConverted,
                    // ДОБАВЬ ЭТУ СТРОКУ:
                    trackTimeMillis = track.trackTimeMillis.toLong(),
                    artworkUrl100 = track.artworkUrl100,
                    previewUrl = track.previewUrl
                )
            )
        }

        insertPlaylistTrackRelation(PlaylistTrackRelation(playlistId, track.trackId))
        return true
    }

    @Transaction
    suspend fun removeTrackFromPlaylist(playlistId: Int, trackId: Int) {
        deletePlaylistTrackRelation(playlistId, trackId)

        // Проверка, остался ли трек в избранных
        val track = getTrackById(trackId)
        if (track != null && track.isFavorite == 0) {
            // Проверка, есть ли трек в других плейлистах
            val playlistCount = getPlaylistCountForTrack(trackId)

            // Если не в любимых и не в плейлистах - удалить
            if (playlistCount == 0) {
                deleteTrack(track)
            }
        }
    }
}