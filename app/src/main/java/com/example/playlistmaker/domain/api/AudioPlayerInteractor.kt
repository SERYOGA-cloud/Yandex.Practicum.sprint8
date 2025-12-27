package com.example.playlistmaker.domain.api

interface AudioPlayerInteractor {

    fun onPlayButtonClicked()

    fun pausePlayer()

    fun getCurrentPositionConverted() : String

    fun releasePlayer()
}