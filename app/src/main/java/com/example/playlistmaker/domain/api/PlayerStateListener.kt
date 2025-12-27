package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.entity.PlayerState

fun interface PlayerStateListener {
    fun onStateChanged(state: PlayerState)
}