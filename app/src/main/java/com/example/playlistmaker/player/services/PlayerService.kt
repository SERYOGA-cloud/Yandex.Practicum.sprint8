package com.example.playlistmaker.player.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.api.AudioPlayerControl
import com.example.playlistmaker.player.domain.entity.PlayerState
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.util.ARGS_TRACK
import com.example.playlistmaker.util.PLAYER_NOTIFICATION_CHANNEL_ID
import com.example.playlistmaker.util.TIME_REFRESH_DELAY
import com.example.playlistmaker.util.Util.Companion.millisToMmSs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerService : Service(), AudioPlayerControl {

    private var track: Track? = null
    private var previewUrl = ""

    private val binder = PlayerServiceBinder()

    private var player: MediaPlayer? = null
    private var timerJob: Job? = null

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default)

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
        createNotificationChannel()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder {
        @Suppress("DEPRECATION")
        track = intent?.getParcelableExtra(ARGS_TRACK)
        previewUrl = track?.previewUrl ?: ""
        preparePlayer()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        stopForegroundNotification()
        return super.onUnbind(intent)
    }

    override fun startPlayer() {
        player?.start()
        _playerState.update { PlayerState.Playing(getCurrentPosition()) }
        startTimer()
    }

    override fun pausePlayer() {
        player?.let { player ->
            try {
                if (isValidStateForPause()) {
                    player.pause()
                    stopForegroundNotification()
                    stopTimer()
                    _playerState.update { PlayerState.Paused(getCurrentPosition()) }
                }
            } catch (e: IllegalStateException) {
                Log.e("AudioPlayer", "Cannot pause in current state", e)
                recoverFromError()
            }
        }
    }

    override fun getPlayerState(): StateFlow<PlayerState> = _playerState.asStateFlow()

    override fun onPlayButtonClicked() {
        when (_playerState.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }

            is PlayerState.Default -> {
                throw Exception("Ошибка, плеер не подготовлен")
            }

            else -> {
                startPlayer()
            }
        }
    }

    override fun startForegroundNotification() {
        if (player?.isPlaying == true) {
            ServiceCompat.startForeground(
                this,
                SERVICE_NOTIFICATION_ID,
                createServiceNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        }
    }

    override fun stopForegroundNotification() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            PLAYER_NOTIFICATION_CHANNEL_ID,
            getString(R.string.audio_player_service),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = getString(R.string.audio_player_service_is_running)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, PLAYER_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("${track?.artistName} - ${track?.trackName}")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun preparePlayer() {
        if (previewUrl.isEmpty()) {
            return
        }
        try {
            player?.setDataSource(track?.previewUrl)
            player?.prepareAsync()
            player?.setOnPreparedListener {
                _playerState.update { PlayerState.Prepared }
            }
            player?.setOnCompletionListener {
                stopTimer()
                stopForegroundNotification()
                _playerState.update { PlayerState.Prepared }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private fun releasePlayer() {
        stopTimer()
        player?.stop()
        player?.setOnPreparedListener(null)
        player?.setOnCompletionListener(null)
        player?.release()
        player = null
        _playerState.update { PlayerState.Default }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Default).launch {

            while (player?.isPlaying == true) {
                delay(TIME_REFRESH_DELAY)
                _playerState.update { PlayerState.Playing(getCurrentPosition()) }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    private fun getCurrentPosition(): String {
        return millisToMmSs(player?.currentPosition ?: -1)
    }

    private fun isValidStateForPause(): Boolean {
        return player?.isPlaying == true
    }

    private fun recoverFromError() {
        releasePlayer()
    }

    inner class PlayerServiceBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    companion object {
        const val SERVICE_NOTIFICATION_ID = 100
    }
}