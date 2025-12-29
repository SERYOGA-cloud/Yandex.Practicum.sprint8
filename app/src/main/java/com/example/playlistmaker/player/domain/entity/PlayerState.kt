package com.example.playlistmaker.player.domain.entity

sealed interface PlayerState {
    data object PLAYING : PlayerState
    data object PAUSED : PlayerState
    data object PREPARED : PlayerState
    data object DEFAULT : PlayerState
}