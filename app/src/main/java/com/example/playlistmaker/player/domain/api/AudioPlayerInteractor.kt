package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.entity.PlayerState
import com.example.playlistmaker.player.domain.listener.PlayerStateListener

interface AudioPlayerInteractor {

    fun onPlayButtonClicked() : PlayerState

    fun pausePlayer()

    fun getCurrentPositionConverted() : String

    fun releasePlayer()

    fun setRepoPlayerStateListener(listener: PlayerStateListener)

    fun preparePlayer(url: String?)
}