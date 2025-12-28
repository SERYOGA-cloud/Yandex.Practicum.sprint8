package com.example.playlistmaker.player.domain.api

interface AudioPlayerInteractor {

    fun onPlayButtonClicked()

    fun pausePlayer()

    fun getCurrentPositionConverted() : String

    fun releasePlayer()
}