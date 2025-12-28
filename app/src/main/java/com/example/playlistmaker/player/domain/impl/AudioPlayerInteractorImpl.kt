package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.playlistmaker.player.domain.api.PlayerStateListener
import com.example.playlistmaker.player.domain.entity.PlayerState
import com.example.playlistmaker.util.Util.Companion.millisToMmSs

class AudioPlayerInteractorImpl(
    private val repository: AudioPlayerRepository,
    private val listener: PlayerStateListener
) : AudioPlayerInteractor {

    override fun onPlayButtonClicked() {
        val playerState = repository.getPlayerState()

        when (playerState) {
            is PlayerState.PLAYING -> {
                pausePlayer()
            }

            is PlayerState.DEFAULT -> {
                throw Exception("Ошибка, плеер не подготовлен")
            }

            else -> {
                startPlayer()
            }
        }
    }

    override fun pausePlayer() {
        repository.pausePlayer()
        listener.onStateChanged(PlayerState.PAUSED)
    }

    override fun getCurrentPositionConverted(): String {
        return millisToMmSs(repository.getCurrentPosition())
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }

    private fun startPlayer() {
        repository.startPlayer()
        listener.onStateChanged(PlayerState.PLAYING)
    }
}
