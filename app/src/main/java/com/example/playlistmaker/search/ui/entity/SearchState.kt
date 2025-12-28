package com.example.playlistmaker.search.ui.entity

import com.example.playlistmaker.search.domain.entity.Track

sealed class SearchState(
    open val isShowLoading: Boolean = false,
    open val isShowSearchResults: Boolean = false,
    open val isShowHistory: Boolean = false,
    open val isShowPlaceHolder: Boolean = false,
    open val showTryAgainButton: Boolean = false,
) {
    data object Empty : SearchState()

    data object Loading : SearchState(isShowLoading = true)

    data class SearchResults(val tracks: List<Track>) : SearchState(isShowSearchResults = true)

    sealed class PlaceHolder(override val showTryAgainButton: Boolean = false) :
        SearchState(isShowPlaceHolder = true, showTryAgainButton = showTryAgainButton) {

        data object NothingFound : PlaceHolder()
        data object NetworkError : PlaceHolder(showTryAgainButton = true)
    }

    data class History(val trackHistory: List<Track>) :
        SearchState(isShowHistory = trackHistory.isNotEmpty())
}
