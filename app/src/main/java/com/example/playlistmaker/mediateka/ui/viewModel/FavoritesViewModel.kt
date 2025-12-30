package com.example.playlistmaker.mediateka.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.mediateka.ui.entity.FavoritesState

class FavoritesViewModel : ViewModel() {

    private val _stateLiveData = MutableLiveData<FavoritesState>(FavoritesState.Placeholder)
    fun observeState(): LiveData<FavoritesState> = _stateLiveData
}