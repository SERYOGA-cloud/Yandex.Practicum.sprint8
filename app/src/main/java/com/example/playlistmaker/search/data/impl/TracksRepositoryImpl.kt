package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.data.mapper.TrackListFromDtoMapper
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.search.domain.entity.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {

        val response = networkClient.doRequest(TrackSearchRequest(expression))

        if (response is TrackSearchResponse) {
            when {
                response.resultCode != 200 -> {
                    emit(Resource.Error(response.resultCode))
                }

                response.results.isNotEmpty() -> {
                    emit(Resource.Success(results = TrackListFromDtoMapper.map(response.results)))
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