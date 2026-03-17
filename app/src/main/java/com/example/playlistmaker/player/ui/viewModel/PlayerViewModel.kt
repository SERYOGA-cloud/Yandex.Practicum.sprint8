package com.example.playlistmaker.player.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.entity.PlayerState
import com.example.playlistmaker.player.domain.listener.PlayerStateListener
import com.example.playlistmaker.player.ui.entity.PlayerScreenState
import com.example.playlistmaker.search.domain.api.TracksDbInteractor
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.util.SingleLiveEvent
import com.example.playlistmaker.util.TIME_REFRESH_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: AudioPlayerInteractor,
    private val tracksDbInteractor: TracksDbInteractor,
) : ViewModel() {

    private var track: Track? = null

    private val onStateChangedListener = PlayerStateListener { newPlayerState ->
        val currentScreenState = screenStateLiveData.value ?: PlayerScreenState.DEFAULT
        screenStateLiveData.value = currentScreenState.copy(playerState = newPlayerState)

        if (newPlayerState == PlayerState.PREPARED) {
            postStatePlaying(false)
            resetTimer()
        }
    }

    private var timerJob: Job? = null

    private val screenStateLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.DEFAULT)
    fun observeScreenState(): LiveData<PlayerScreenState> = screenStateLiveData

    private val errorMessageLiveData = SingleLiveEvent<String>()
    fun observeErrorMessage(): LiveData<String> = errorMessageLiveData

    override fun onCleared() {
        super.onCleared()
        playerInteractor.releasePlayer()
    }

    fun initialize(track: Track) {
        this.track = track
        playerInteractor.setRepoPlayerStateListener(onStateChangedListener)
        playerInteractor.preparePlayer(track.previewUrl)
        updateIsFavorite()
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
        timerJob?.cancel()
    }

    fun onPlayButtonClicked() {
        try {
            val playerStateAfterClick = playerInteractor.onPlayButtonClicked()

            when (playerStateAfterClick) {
                PlayerState.DEFAULT -> {}

                PlayerState.PAUSED -> {
                    postStatePlaying(false)
                    pauseTimer()
                }

                PlayerState.PLAYING -> {
                    postStatePlaying(true)
                    startTimer()
                }

                PlayerState.PREPARED -> {
                    postStatePlaying(false)
                    resetTimer()
                }
            }
        } catch (e: Exception) {
            errorMessageLiveData.postValue(e.message)
        }
    }

    fun onFavoriteButtonClicked() {
        if (track == null) {
            return
        }
        val isFavorite = screenStateLiveData.value!!.isFavorite()

        if (isFavorite) {
            viewModelScope.launch {
                track!!.isFavorite = false
                tracksDbInteractor.deleteFromFavorites(track!!)
                updateIsFavorite()
            }
        } else {
            viewModelScope.launch {
                track!!.isFavorite = true
                tracksDbInteractor.addToFavorites(track!!)
                updateIsFavorite()
            }
        }
    }

    //  Это про жизненный цикл фрагмента
    fun pause() {
        pauseTimer()
        playerInteractor.pausePlayer()
    }

    private fun postStatePlaying(isPlaying: Boolean) {
        val currentScreenState: PlayerScreenState = screenStateLiveData.value!!
        screenStateLiveData.value = currentScreenState.copy(isPlayButtonShown = !isPlaying)
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {

            while (screenStateLiveData.value?.getPlayerState() is PlayerState.PLAYING) {
                delay(TIME_REFRESH_DELAY)
                val currentPosition: String = playerInteractor.getCurrentPositionConverted()
                val currentScreenState: PlayerScreenState = screenStateLiveData.value!!
                screenStateLiveData.value =
                    currentScreenState.copy(currentPosition = currentPosition)
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private fun resetTimer() {
        timerJob?.cancel()
        val currentScreenState = screenStateLiveData.value ?: PlayerScreenState.DEFAULT
        screenStateLiveData.value = currentScreenState.copy(currentPosition = PROGRESS_TIME_DEFAULT)
    }

    private fun updateIsFavorite() {
        if (track == null) {
            return
        } else {
            val currentScreenState = screenStateLiveData.value ?: PlayerScreenState.DEFAULT
            screenStateLiveData.value = currentScreenState.copy(isFavorite = track!!.isFavorite)
        }
    }

    companion object {
        private const val PROGRESS_TIME_DEFAULT = "00:00"
    }
}