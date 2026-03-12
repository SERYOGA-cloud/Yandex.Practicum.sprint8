package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.entity.PlayerState
import com.example.playlistmaker.player.domain.listener.PlayerStateListener

interface AudioPlayerRepository {

    fun startPlayer()

    fun pausePlayer()

    fun getCurrentPosition(): Int

    fun getPlayerState(): PlayerState

    fun releasePlayer()

    fun setPlayerStateListener(listener: PlayerStateListener)

    fun preparePlayer(url: String?)
}