package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.entity.PlayerState

interface AudioPlayerRepository {

    fun startPlayer()

    fun pausePlayer()

    fun getCurrentPosition(): Int

    fun getPlayerState(): PlayerState

    fun releasePlayer()

}