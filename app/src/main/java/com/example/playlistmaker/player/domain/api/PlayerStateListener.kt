package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.entity.PlayerState

fun interface PlayerStateListener {
    fun onStateChanged(state: PlayerState)
}