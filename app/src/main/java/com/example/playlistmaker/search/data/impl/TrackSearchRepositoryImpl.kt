package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.db.AppDataBase
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.data.mapper.TrackListFromDtoMapper
import com.example.playlistmaker.search.domain.api.TrackSearchRepository
import com.example.playlistmaker.search.domain.entity.Resource
import com.example.playlistmaker.search.domain.entity.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TrackSearchRepositoryImpl(
    private val dataBase: AppDataBase,
    private val networkClient: NetworkClient
) : TrackSearchRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {

        val response = networkClient.doRequest(TrackSearchRequest(expression))

        if (response is TrackSearchResponse) {
            when {
                response.resultCode != 200 -> {
                    emit(Resource.Error(response.resultCode))
                }

                response.results.isNotEmpty() -> {
                    dataBase.trackDao().getAllTrackIds()
                        .map { allFavoriteIds ->
                            TrackListFromDtoMapper.map(response.results, allFavoriteIds)
                        }
                        .collect { results ->
                            emit(Resource.Success(results))
                        }
                }

                else -> {
                    emit(Resource.Success(emptyList()))
                }
            }
        } else {
            emit(Resource.Error(-1))
        }
    }
}