package com.example.playlistmaker.search.ui.entity

import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.entity.Track

sealed interface SearchState {

    data object Empty : SearchState

    data object Loading : SearchState

    data class Content(val tracks: List<Track>) : SearchState

    sealed class PlaceHolder(
        open val imageId: Int, open val textId: Int
    ) : SearchState {

        data class NothingFound(
            override val imageId: Int = R.drawable.img_nothing_found,
            override val textId: Int = R.string.nothing_found
        ) : PlaceHolder(imageId = imageId, textId = textId)

        data class NetworkError(
            override val imageId: Int = R.drawable.img_connection_issues,
            override val textId: Int = R.string.connection_issues_check_connection
        ) : PlaceHolder(imageId = imageId, textId = textId)
    }

    data class History(val trackHistory: List<Track>) : SearchState
}
