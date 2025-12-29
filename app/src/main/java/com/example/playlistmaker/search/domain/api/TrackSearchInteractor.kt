package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.consumer.TrackConsumer

interface TrackSearchInteractor {

    fun searchTracks(expression: String, consumer: TrackConsumer)
}
