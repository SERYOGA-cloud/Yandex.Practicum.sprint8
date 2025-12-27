package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.consumer.TrackConsumer

interface TrackSearchInteractor {

    fun searchTracks(expression: String, consumer: TrackConsumer)
}
