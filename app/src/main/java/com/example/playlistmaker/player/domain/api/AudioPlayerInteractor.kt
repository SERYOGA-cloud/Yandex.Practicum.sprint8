package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.entity.PlayerState

interface AudioPlayerInteractor {

    fun onPlayButtonClicked() : PlayerState

    fun pausePlayer()

    fun getCurrentPositionConverted() : String

    fun releasePlayer()
}