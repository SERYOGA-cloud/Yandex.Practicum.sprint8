package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TrackSearchInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.consumer.TrackConsumer
import java.util.concurrent.Executors

class TrackSearchInteractorImpl(private val repository: TracksRepository) : TrackSearchInteractor {

    private val executor = Executors.newSingleThreadExecutor()

    override fun searchTracks(expression: String, consumer: TrackConsumer) {
        executeSearchInNewThread(expression, consumer)
    }

    private fun executeSearchInNewThread(expression: String, consumer: TrackConsumer) {
        executor.execute {
            val result = repository.searchTracks(expression)
            consumer.consume(result)
        }
    }
}