package com.example.playlistmaker.search.domain.consumer

import com.example.playlistmaker.search.domain.entity.Resource
import com.example.playlistmaker.search.domain.entity.Track

fun interface TrackConsumer {
    fun consume(result: Resource<List<Track>>)
}