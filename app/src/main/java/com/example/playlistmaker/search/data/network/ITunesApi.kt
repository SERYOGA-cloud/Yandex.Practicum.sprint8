package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("/search?entity=song")
    fun search(@Query("term") query: String): Call<TrackSearchResponse>
}