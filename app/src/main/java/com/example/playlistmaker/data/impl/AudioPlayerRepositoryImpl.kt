package com.example.playlistmaker.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.AudioPlayerRepository
import com.example.playlistmaker.domain.entity.PlayerState

class AudioPlayerRepositoryImpl(private val previewUrl: String?) : AudioPlayerRepository {

    private val player = MediaPlayer()
    private var playerState: PlayerState = PlayerState.DEFAULT

    init {
        preparePlayer()
    }

    override fun startPlayer() {
        player.start()
        playerState = PlayerState.PLAYING
    }

    override fun pausePlayer() {
        player.pause()
        playerState = PlayerState.PAUSED
    }

    override fun getCurrentPosition(): Int {
        return player.currentPosition
    }

    override fun getPlayerState(): PlayerState {
        return playerState
    }

    override fun releasePlayer() {
        player.release()
    }

    private fun preparePlayer() {
        if (previewUrl == null) {
            throw Exception("Ошибка. Отсутствует ссылка на отрывок трека")
        }
        player.setDataSource(previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerState = PlayerState.PREPARED
        }
        player.setOnCompletionListener {
            playerState = PlayerState.PREPARED
        }
    }
}
