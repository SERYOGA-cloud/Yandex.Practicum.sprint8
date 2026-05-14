package com.example.playlistmaker.mediateka.favorites.ui.composable


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.playlistmaker.R
import com.example.playlistmaker.mediateka.favorites.ui.entity.FavoritesState
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.search.ui.composable.TrackList

@Composable
fun FavoritesContent(state: FavoritesState.Content, onItemClicked: (track: Track) -> Unit) {

    Column(Modifier.fillMaxSize()) {
        Spacer(
            Modifier
                .height(dimensionResource(R.dimen.favorites_content_top_spacer))
        )

        TrackList(
            state.tracks,
            onItemCLicked = { onItemClicked(it) }
        )
    }
}