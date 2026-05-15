package com.example.playlistmaker.mediateka.favorites.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.example.playlistmaker.mediateka.favorites.ui.entity.FavoritesState.Content
import com.example.playlistmaker.mediateka.favorites.ui.entity.FavoritesState.Placeholder
import com.example.playlistmaker.mediateka.favorites.ui.viewModel.FavoritesViewModel
import com.example.playlistmaker.search.domain.entity.Track
import org.koin.androidx.compose.koinViewModel


@Composable
fun FavoritesPage(bottomNavHeight: Dp, onTrackClicked: (track: Track) -> Unit) {

    val viewModel = koinViewModel<FavoritesViewModel>()
    val state = viewModel.observeState().observeAsState().value

    Column(Modifier
        .fillMaxSize()
        .padding(bottom = bottomNavHeight)) {

        when (state) {
            is Content -> FavoritesContent(state) { track -> onTrackClicked(track) }

            is Placeholder -> com.example.playlistmaker.search.ui.composable.Placeholder(
                textId = state.textId,
                imageId = state.imageId
            )

            null -> {}
        }
    }
}