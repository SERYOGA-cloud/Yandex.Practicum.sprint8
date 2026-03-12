package com.example.playlistmaker.mediateka.ui.entity

import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.entity.Track

sealed interface FavoritesState {

    data class Placeholder(
        val textId: Int = R.string.your_mediateka_is_empty
    ) : FavoritesState

    data class Content(val tracks: List<Track>) : FavoritesState
}