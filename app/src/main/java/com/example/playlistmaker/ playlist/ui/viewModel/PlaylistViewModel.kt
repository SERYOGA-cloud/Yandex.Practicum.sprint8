package com.example.playlistmaker.playlist.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.playlists.domain.api.PlaylistsInteractor
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.playlist.ui.entity.PlaylistScreenState
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.api.StringResourceProvider
import com.example.playlistmaker.util.SingleLiveEvent
import kotlinx.coroutines.launch

class PlaylistViewModel(
    val playlistsInteractor: PlaylistsInteractor,
    val sharingInteractor: SharingInteractor,
    val stringResourceProvider: StringResourceProvider,
    val initialPlaylist: Playlist
) : ViewModel() {

    private val playlistId = initialPlaylist.id

    private val _playlistScreenState =
        MutableLiveData<PlaylistScreenState>(PlaylistScreenState.Empty)

    private val _tracks = MutableLiveData<List<Track>>()
    private val _playlist = MutableLiveData<Playlist>(initialPlaylist)
    private val _shouldCloseScreen = SingleLiveEvent<Unit>()
    private val _toastMessage = SingleLiveEvent<String>()

    fun observeBottomSheetState(): LiveData<PlaylistScreenState> = _playlistScreenState
    fun observeTracks(): LiveData<List<Track>> = _tracks
    fun observePlaylist(): LiveData<Playlist> = _playlist
    fun observeShouldCloseScreen(): LiveData<Unit> = _shouldCloseScreen
    fun observeToastMessage(): LiveData<String> = _toastMessage

    init {
        loadTracks()
    }

    fun onMenuButtonClicked() {
        _playlistScreenState.value = PlaylistScreenState.MenuBottomSheet
    }

    fun onSharing() {
        if (_tracks.value.isNullOrEmpty()) {
            _toastMessage.value = stringResourceProvider.getNoTracksInPlaylistToShareMsg()
        } else {
            sharingInteractor.sharePlaylist(_playlist.value!!)
        }
    }

    fun onOverlayClicked() {
        val tracks = _tracks.value
        postContentBottomSheetOrEmpty(tracks ?: emptyList())
    }

    fun onDeleteTrack(track: Track) {
        viewModelScope.launch {
            playlistsInteractor.deleteTrackFromPlaylist(playlistId, track.trackId)
            refreshPlaylist()
            loadTracks()
        }
    }

    fun onDeletePlaylist() {
        viewModelScope.launch {
            val currentPlaylist = _playlist.value!!

            playlistsInteractor.deletePlaylist(currentPlaylist)
            _shouldCloseScreen.postValue(Unit)
        }
    }

    fun refreshPlaylist() {
        viewModelScope.launch {
            playlistsInteractor.getPlaylistById(playlistId).collect {
                _playlist.postValue(it)
            }
        }
    }

    private fun loadTracks() {
        viewModelScope.launch {
            playlistsInteractor.getTracksByPlaylistId(playlistId).collect { tracks ->
                postContentBottomSheetOrEmpty(tracks)
                _tracks.value = tracks
            }
        }
    }

    private fun postContentBottomSheetOrEmpty(tracks: List<Track>) {
        if (tracks.isNotEmpty()) {
            _playlistScreenState.value = PlaylistScreenState.ContentBottomSheet
        } else {
            _playlistScreenState.value = PlaylistScreenState.Empty
        }
    }
}