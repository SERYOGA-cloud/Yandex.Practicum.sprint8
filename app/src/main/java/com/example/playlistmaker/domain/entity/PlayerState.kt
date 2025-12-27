package com.example.playlistmaker.domain.entity

sealed interface PlayerState {
    data object PLAYING : PlayerState
    data object PAUSED : PlayerState
    data object PREPARED : PlayerState
    data object DEFAULT : PlayerState
}