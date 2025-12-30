package com.example.playlistmaker.mediateka.ui.entity

sealed interface FavoritesState {

    data object Placeholder: FavoritesState
}