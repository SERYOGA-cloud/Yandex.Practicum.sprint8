package com.example.playlistmaker.mediateka.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.ui.entity.FavoritesState
import com.example.playlistmaker.search.domain.api.TracksDbInteractor
import kotlinx.coroutines.launch

class FavoritesViewModel(private val dataBaseInteractor: TracksDbInteractor) : ViewModel() {

    private val _stateLiveData = MutableLiveData<FavoritesState>()
    fun observeState(): LiveData<FavoritesState> = _stateLiveData

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            dataBaseInteractor.getFavorites().collect { result ->
                Log.d("FAVORITES", "result = $result")
                if (result.isNotEmpty()) {
                    _stateLiveData.value = FavoritesState.Content(result)
                    Log.d("FAVORITES", "Not empty. state = ${_stateLiveData.value!!::class.java}")
                } else {
                    _stateLiveData.value = FavoritesState.Placeholder()
                    Log.d("FAVORITES", "Empty. state = ${_stateLiveData.value!!::class.java}")
                }
            }
        }
    }
}