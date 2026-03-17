package com.example.playlistmaker.player.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.entity.PlayerState
import com.example.playlistmaker.player.domain.listener.PlayerStateListener
import com.example.playlistmaker.player.ui.entity.PlayerScreenState
import com.example.playlistmaker.mediateka.favorites.domain.api.TracksInteractor
import com.example.playlistmaker.mediateka.playlists.domain.api.PlaylistsInteractor
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.sharing.domain.api.StringResourceProvider
import com.example.playlistmaker.util.SingleLiveEvent
import com.example.playlistmaker.util.TIME_REFRESH_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: AudioPlayerInteractor,
    private val tracksInteractor: TracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
    private val stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private var track: Track? = null

    private val onStateChangedListener = PlayerStateListener { newPlayerState ->
        val currentScreenState = _screenStateLiveData.value ?: PlayerScreenState.Default
        _screenStateLiveData.value = currentScreenState.copy(playerState = newPlayerState)

        if (newPlayerState == PlayerState.PREPARED) {
            postStatePlaying(false)
            resetTimer()
        }
    }

    private var timerJob: Job? = null

    private val _screenStateLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.Default)
    fun observeScreenState(): LiveData<PlayerScreenState> = _screenStateLiveData

    private val _toastMessageLiveData = SingleLiveEvent<String>()
    fun observeToastMessage(): LiveData<String> = _toastMessageLiveData

    override fun onCleared() {
        super.onCleared()
        playerInteractor.releasePlayer()
    }

    fun initializeTrack(track: Track) {
        this.track = track
        playerInteractor.setRepoPlayerStateListener(onStateChangedListener)
        playerInteractor.preparePlayer(track.previewUrl)
        updateIsFavorite()
    }

    fun updatePlaylists() {
        viewModelScope.launch {
            playlistsInteractor.getAllPlaylists().collect { playlists ->
                val currentState = _screenStateLiveData.value
                _screenStateLiveData.value = currentState?.copy(playlists = playlists)
            }
        }
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
            _toastMessageLiveData.postValue(e.message)
        }
    }

    fun onFavoriteButtonClicked() {
        if (track == null) {
            return
        }
        val isFavoriteCurrent = _screenStateLiveData.value!!.isFavorite

        viewModelScope.launch {
            track!!.isFavorite = !isFavoriteCurrent
        }
        if (isFavoriteCurrent) {
            viewModelScope.launch {
                track!!.isFavorite = false
                tracksInteractor.deleteFromFavorites(track!!)
                updateIsFavorite()
            }
        } else {
            viewModelScope.launch {
                track!!.isFavorite = true
                tracksInteractor.addToDataBase(track!!)
                updateIsFavorite()
            }
        }
    }

    fun onAddToPlaylistButtonClicked() {
        viewModelScope.launch {
            val currentState = _screenStateLiveData.value
            _screenStateLiveData.value = currentState?.copy(isBottomSheetVisible = true)
        }
    }

    fun onOverlayClicked() {
        val currentState = _screenStateLiveData.value
        _screenStateLiveData.value = currentState?.copy(isBottomSheetVisible = false)
    }

    fun onNewPlaylistButtonClicked() {
        val currentState = _screenStateLiveData.value
        _screenStateLiveData.value = currentState?.copy(isBottomSheetVisible = false)
        pause()
    }

    //  Это про жизненный цикл фрагмента
    fun pause() {
        pauseTimer()
        playerInteractor.pausePlayer()
    }

    fun addTrackToPlaylist(playlist: Playlist) {
        if (track == null) {
            return
        }
        viewModelScope.launch {
            val isSuccessfullyAdded = playlistsInteractor.addTrackToPlaylist(playlist, track!!)
            if (!isSuccessfullyAdded) {
                val message = stringResourceProvider.getTrackAlreadyAddedMsg(playlist.title)
                _toastMessageLiveData.value = message
            } else {
                val message = stringResourceProvider.getTrackAddedSuccessfullyMsg(playlist.title)
                _toastMessageLiveData.value = message
            }
        }
    }

    private fun postStatePlaying(isPlaying: Boolean) {
        val currentScreenState: PlayerScreenState = _screenStateLiveData.value!!
        _screenStateLiveData.value = currentScreenState.copy(isPlayButtonShown = !isPlaying)
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {

            while (_screenStateLiveData.value?.playerState is PlayerState.PLAYING) {
                delay(TIME_REFRESH_DELAY)
                val currentPosition: String = playerInteractor.getCurrentPositionConverted()
                val currentScreenState: PlayerScreenState = _screenStateLiveData.value!!
                _screenStateLiveData.value =
                    currentScreenState.copy(currentPosition = currentPosition)
            }
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private fun resetTimer() {
        timerJob?.cancel()
        val currentScreenState = _screenStateLiveData.value ?: PlayerScreenState.Default
        _screenStateLiveData.value =
            currentScreenState.copy(currentPosition = PROGRESS_TIME_DEFAULT)
    }

    private fun updateIsFavorite() {
        if (track == null) {
            return
        } else {
            val currentScreenState = _screenStateLiveData.value ?: PlayerScreenState.Default
            _screenStateLiveData.value = currentScreenState.copy(isFavorite = track!!.isFavorite)
        }
    }

    companion object {
        private const val PROGRESS_TIME_DEFAULT = "00:00"
    }
}