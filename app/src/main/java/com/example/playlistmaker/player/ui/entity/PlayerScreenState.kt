package com.example.playlistmaker.player.ui.entity

import com.example.playlistmaker.player.domain.entity.PlayerState

data class PlayerScreenState(
    private val currentPosition: String,
    private val playerState: PlayerState,
    private val isFavorite: Boolean,
    private val isPlayButtonShown: Boolean
) {

    fun getPlayerState(): PlayerState = playerState
    fun getCurrentPosition() : String = currentPosition
    fun isFavorite(): Boolean = isFavorite
    fun isPlayButtonShown(): Boolean = isPlayButtonShown

    companion object {
        val DEFAULT = PlayerScreenState(
            currentPosition = "00:00",
            playerState = PlayerState.DEFAULT,
            isFavorite = false,
            isPlayButtonShown = true
        )
    }
}