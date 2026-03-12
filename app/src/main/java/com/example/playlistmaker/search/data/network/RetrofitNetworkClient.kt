package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val iTunesService: ITunesApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {

        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = -1 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val retrofitResponse = iTunesService.search(dto.expression)
                retrofitResponse.apply { resultCode = 200 }
            } catch (e: Exception) {
                Response().apply { resultCode = -1 }
            }
        }
    }
}