package com.example.playlistmaker.domain.consumer

import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track

fun interface TrackConsumer {
    fun consume(result: Resource<List<Track>>)
}