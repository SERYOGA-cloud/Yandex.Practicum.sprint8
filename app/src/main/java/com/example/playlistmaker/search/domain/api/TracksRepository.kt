package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.entity.Resource
import com.example.playlistmaker.search.domain.entity.Track

interface TracksRepository {
    fun searchTracks(expression: String) : Resource<List<Track>>
}