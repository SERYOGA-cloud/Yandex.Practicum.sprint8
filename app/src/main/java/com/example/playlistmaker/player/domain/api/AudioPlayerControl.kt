package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.entity.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun startPlayer()
    fun pausePlayer()
    fun onPlayButtonClicked()
    fun getPlayerState(): StateFlow<PlayerState>
    fun startForegroundNotification()
    fun stopForegroundNotification()
}