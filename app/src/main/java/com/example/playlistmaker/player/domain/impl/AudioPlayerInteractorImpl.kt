package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.playlistmaker.player.domain.listener.PlayerStateListener
import com.example.playlistmaker.player.domain.entity.PlayerState
import com.example.playlistmaker.util.Util.Companion.millisToMmSs

class AudioPlayerInteractorImpl(
    private val repository: AudioPlayerRepository
) : AudioPlayerInteractor {

    override fun onPlayButtonClicked(): PlayerState {

        when (repository.getPlayerState()) {
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

        return repository.getPlayerState()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun getCurrentPositionConverted(): String {
        return millisToMmSs(repository.getCurrentPosition())
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }

    private fun startPlayer() {
        repository.startPlayer()
    }

    override fun setRepoPlayerStateListener(listener: PlayerStateListener) {
        repository.setPlayerStateListener(listener)
    }
}
