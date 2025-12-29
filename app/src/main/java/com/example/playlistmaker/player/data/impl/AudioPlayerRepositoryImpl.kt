package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.playlistmaker.player.domain.listener.PlayerStateListener
import com.example.playlistmaker.player.domain.entity.PlayerState

class AudioPlayerRepositoryImpl(
    private val previewUrl: String,
    private val player: MediaPlayer
) : AudioPlayerRepository {

    private var playerState: PlayerState = PlayerState.DEFAULT
    private var playerStateListener: PlayerStateListener? = null

    init {
        preparePlayer()
    }

    override fun startPlayer() {
        player.start()
        playerState = PlayerState.PLAYING
        notifyListener()
    }

    override fun pausePlayer() {
        player.pause()
        playerState = PlayerState.PAUSED
        notifyListener()
    }

    override fun getCurrentPosition(): Int = player.currentPosition

    override fun getPlayerState(): PlayerState = playerState

    override fun releasePlayer() {
        player.release()
    }

    override fun setPlayerStateListener(listener: PlayerStateListener) {
        playerStateListener = listener
    }

    private fun preparePlayer() {
        player.setDataSource(previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerState = PlayerState.PREPARED
            notifyListener()
        }
        player.setOnCompletionListener {
            playerState = PlayerState.PREPARED
            notifyListener()
        }
    }

    private fun notifyListener() {
        playerStateListener?.onStateChanged(playerState)
    }
}