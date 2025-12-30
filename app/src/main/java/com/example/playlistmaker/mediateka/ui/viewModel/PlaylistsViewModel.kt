package com.example.playlistmaker.mediateka.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.mediateka.ui.entity.PlaylistsState

class PlaylistsViewModel : ViewModel() {

    private val _stateLiveData = MutableLiveData<PlaylistsState>(PlaylistsState.Placeholder)
    fun observeState(): LiveData<PlaylistsState> = _stateLiveData
}