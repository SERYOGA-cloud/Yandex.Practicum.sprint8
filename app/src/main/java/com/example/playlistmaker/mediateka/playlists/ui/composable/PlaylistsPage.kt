package com.example.playlistmaker.mediateka.playlists.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.playlistmaker.R
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.mediateka.playlists.ui.entity.PlaylistsState.Content
import com.example.playlistmaker.mediateka.playlists.ui.entity.PlaylistsState.Placeholder
import com.example.playlistmaker.mediateka.playlists.ui.viewModel.PlaylistsViewModel
import com.example.playlistmaker.search.ui.composable.CustomButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistsPage(
    bottomNavHeight: Dp,
    onButtonClicked: () -> Unit,
    onItemClicked: (item: Playlist) -> Unit
) {

    val viewModel: PlaylistsViewModel = koinViewModel()
    val state = viewModel.observeState().observeAsState().value

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.playlists_page_padding_horizontal))
            .padding(bottom = bottomNavHeight),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(dimensionResource(R.dimen.playlists_button_top_spacer)))

        CustomButton(
            text = stringResource(R.string.new_playlist),
            onClick = { onButtonClicked() }
        )

        Spacer(Modifier.height(dimensionResource(R.dimen.playlists_button_bottom_spacer)))

        when (state) {
            is Content -> PlaylistsContent(state) { onItemClicked(it) }

            is Placeholder -> com.example.playlistmaker.search.ui.composable.Placeholder(
                textId = state.textId,
                imageId = state.imageId,
                isPlaylists = true
            )

            null -> {}
        }
    }
}