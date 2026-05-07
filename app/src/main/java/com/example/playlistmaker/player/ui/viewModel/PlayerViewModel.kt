package com.example.playlistmaker.player.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.favorites.domain.api.TracksInteractor
import com.example.playlistmaker.mediateka.playlists.domain.api.PlaylistsInteractor
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.player.domain.api.AudioPlayerControl
import com.example.playlistmaker.player.domain.entity.PlayerState
import com.example.playlistmaker.player.ui.entity.PlayerScreenState
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.sharing.domain.api.StringResourceProvider
import com.example.playlistmaker.util.SingleLiveEvent
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val tracksInteractor: TracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
    private val stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private var track: Track? = null
    private var audioPlayerControl: AudioPlayerControl? = null

    private val _screenStateLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.Default)
    fun observeScreenState(): LiveData<PlayerScreenState> = _screenStateLiveData

    private val _toastMessageLiveData = SingleLiveEvent<String>()
    fun observeToastMessage(): LiveData<String> = _toastMessageLiveData

    fun onAppMinimized() {
        val isPlaying = (_screenStateLiveData.value?.isPlayButtonShown == false)
        if (isPlaying) {
            startForegroundNotification()
        }
    }

        fun onAppResumed() {
            stopForegroundNotification()
        }

        fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
            this.audioPlayerControl = audioPlayerControl

            viewModelScope.launch {
                audioPlayerControl.getPlayerState().collect { newPlayerState ->
                    val isPlaying = newPlayerState is PlayerState.Playing
                    val progress = newPlayerState.progress
                    updatePlayButtonAndProgress(isPlaying, progress)
                }
            }
        }

        fun removeAudioPlayerControl() {
            this.audioPlayerControl = null
    }

    fun initializeTrack(track: Track) {
        this.track = track
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

    fun onPlayButtonClicked() {
        audioPlayerControl?.onPlayButtonClicked()
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

                val currentState = _screenStateLiveData.value
                _screenStateLiveData.value = currentState?.copy(isBottomSheetVisible = false)
            }
        }
    }

    private fun updatePlayButtonAndProgress(isPlaying: Boolean, progress: String) {
        val currentScreenState: PlayerScreenState = _screenStateLiveData.value!!
        _screenStateLiveData.value =
            currentScreenState.copy(isPlayButtonShown = !isPlaying, currentPosition = progress)
    }

    private fun updateIsFavorite() {
        if (track == null) {
            return
        } else {
            val currentScreenState = _screenStateLiveData.value ?: PlayerScreenState.Default
            _screenStateLiveData.value = currentScreenState.copy(isFavorite = track!!.isFavorite)
        }
    }

    private fun startForegroundNotification() {
        audioPlayerControl?.startForegroundNotification()
    }

    private fun stopForegroundNotification() {
        audioPlayerControl?.stopForegroundNotification()
    }
}