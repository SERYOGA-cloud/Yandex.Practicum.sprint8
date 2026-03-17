package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.player.domain.api.AudioPlayerRepository
import com.example.playlistmaker.player.domain.entity.PlayerState
import com.example.playlistmaker.player.domain.listener.PlayerStateListener

class AudioPlayerRepositoryImpl() : AudioPlayerRepository {

    private var player: MediaPlayer? = null

    private var playerState: PlayerState = PlayerState.DEFAULT
    private var playerStateListener: PlayerStateListener? = null

    override fun startPlayer() {
        player?.start()
        playerState = PlayerState.PLAYING
        notifyListener()
    }

    override fun pausePlayer() {
        player?.let { player ->
            try {
                if (isValidStateForPause()) {
                    player.pause()
                    playerState = PlayerState.PAUSED
                    notifyListener()
                }
            } catch (e: IllegalStateException) {
                Log.e("AudioPlayer", "Cannot pause in current state", e)
                recoverFromError()
            }
        }
//        player?.pause()
//        playerState = PlayerState.PAUSED
//        notifyListener()
    }

    override fun getCurrentPosition(): Int {
        return player?.currentPosition ?: -1
    }

    override fun getPlayerState(): PlayerState {
        return playerState
    }

    override fun releasePlayer() {
        player?.release()
        player = null
        playerState = PlayerState.DEFAULT
    }

    override fun setPlayerStateListener(listener: PlayerStateListener) {
        playerStateListener = listener
    }

    override fun preparePlayer(url: String?) {
        try {
            player?.release()
            player = MediaPlayer()

            player?.setDataSource(url)
            player?.prepareAsync()

            player?.setOnPreparedListener {
                playerState = PlayerState.PREPARED
                notifyListener()
            }

            player?.setOnCompletionListener {
                playerState = PlayerState.PREPARED
                notifyListener()
            }

        } catch (e: Exception) {
            throw e
        }
    }

    private fun isValidStateForPause(): Boolean {
        return player?.isPlaying == true
    }

    private fun recoverFromError() {
        releasePlayer()
    }

    private fun notifyListener() {
        playerStateListener?.onStateChanged(playerState)
    }
}