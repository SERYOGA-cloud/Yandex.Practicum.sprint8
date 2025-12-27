package com.example.playlistmaker.data.impl

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.data.mapper.TrackListFromDtoMapper
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.entity.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {

        val response = networkClient.doRequest(TrackSearchRequest(expression))

        if (response is TrackSearchResponse) {
            return when {
                response.resultCode != 200 -> {
                    Resource.Error(response.resultCode)
                }

                response.results.isNotEmpty() -> {
                    Resource.Success(results = TrackListFromDtoMapper.map(response.results))
                }

                else -> {
                    Resource.Success(emptyList())
                }
            }
        } else {
            return Resource.Error(-1)
        }
    }
}