package com.example.playlistmaker.player.ui.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.entity.PlayerState
import com.example.playlistmaker.player.ui.entity.PlayerScreenState
import com.example.playlistmaker.util.SingleLiveEvent

class PlayerViewModel(url: String?) : ViewModel() {

    companion object {
        private const val PROGRESS_TIME_DEFAULT = "00:00"
        private const val TIME_REFRESH_DELAY = 200L

        fun getFactory(url: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(url)
            }
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private val screenStateLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.DEFAULT)
    fun observeScreenState(): LiveData<PlayerScreenState> = screenStateLiveData

    private val errorMessageLiveData = SingleLiveEvent<String>()
    fun observeErrorMessage(): LiveData<String> = errorMessageLiveData

    private val playerInteractor = Creator.provideAudioPlayerInteractor(
        previewUrl = url,
        onStateChangedListener = { newPlayerState ->
            val currentScreenState = screenStateLiveData.value ?: PlayerScreenState.DEFAULT
            screenStateLiveData.value = currentScreenState.copy(playerState = newPlayerState)
        }
    )

    private val timeRefreshRunnable = object : Runnable {
        override fun run() {
            val currentPosition: String = playerInteractor.getCurrentPositionConverted()
            val currentScreenState: PlayerScreenState =
                screenStateLiveData.value ?: PlayerScreenState.DEFAULT
            screenStateLiveData.value = currentScreenState.copy(currentPosition = currentPosition)
            handler.postDelayed(this, TIME_REFRESH_DELAY)
        }

    }

    fun onPlayButtonClicked() {
        try {
            val playerStateAfterClick = playerInteractor.onPlayButtonClicked()

            when (playerStateAfterClick) {
                PlayerState.DEFAULT -> {}
                PlayerState.PAUSED -> pauseTimer()
                PlayerState.PLAYING -> startTimer()
                PlayerState.PREPARED -> resetTimer()
            }
        } catch (e: Exception) {
            errorMessageLiveData.postValue(e.message)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.releasePlayer()
        resetTimer()
    }

    fun onPause() {
        pauseTimer()
        playerInteractor.pausePlayer()
    }

    private fun startTimer() {
        handler.post(timeRefreshRunnable)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(timeRefreshRunnable)
    }

    private fun resetTimer() {
        handler.removeCallbacks(timeRefreshRunnable)
        val currentScreenState = screenStateLiveData.value?: PlayerScreenState.DEFAULT
        screenStateLiveData.value = currentScreenState.copy(currentPosition = PROGRESS_TIME_DEFAULT)
    }
}