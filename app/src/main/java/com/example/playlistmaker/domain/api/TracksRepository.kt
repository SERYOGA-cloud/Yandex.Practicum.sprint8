package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track

interface TracksRepository {
    fun searchTracks(expression: String) : Resource<List<Track>>
}