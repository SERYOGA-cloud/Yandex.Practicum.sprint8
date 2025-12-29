package com.example.playlistmaker.player.ui.entity

import com.example.playlistmaker.player.domain.entity.PlayerState

data class PlayerScreenState(
    private val currentPosition: String,
    private val playerState: PlayerState
) {

    fun getPlayerState(): PlayerState = playerState
    fun getCurrentPosition() : String = currentPosition

    companion object {
        val DEFAULT = PlayerScreenState(
            currentPosition = "00:00",
            playerState = PlayerState.DEFAULT
        )
    }
}