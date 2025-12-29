package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: Any): Response {

        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = -1 }
        }

        try {
            val retrofitResponse = iTunesService.search(dto.expression).execute()

            return retrofitResponse.body()?.apply { resultCode = 200 }
                ?: Response().apply { resultCode = 0 }

        } catch (e: Exception) {
            return Response().apply { resultCode = -1 }
        }
    }
}