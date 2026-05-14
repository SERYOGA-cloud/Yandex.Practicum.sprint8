package com.example.playlistmaker.mediateka.playlists.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.playlists.domain.api.PlaylistsInteractor
import com.example.playlistmaker.mediateka.playlists.ui.entity.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private val _stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = _stateLiveData

    init {
        updateState()
    }

    private fun updateState() {

        viewModelScope.launch {
            playlistsInteractor.getAllPlaylists().collect { dbPlaylists ->
                if (dbPlaylists.isEmpty()) {
                    _stateLiveData.postValue(PlaylistsState.Placeholder())
                } else {
                    _stateLiveData.postValue(
                        PlaylistsState.Content(dbPlaylists)
                    )
                }
            }
        }
    }
}