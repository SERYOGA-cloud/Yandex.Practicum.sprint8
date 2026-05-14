package com.example.playlistmaker.mediateka.playlists.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.mediateka.playlists.ui.entity.PlaylistsState

@Composable
fun PlaylistsContent(state: PlaylistsState.Content, onCLick: (item: Playlist) -> Unit) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        Modifier.fillMaxWidth()
    ) {
        items(
            count = state.playlists.size
        ) { index ->
            val playlist = state.playlists[index]
            PlaylistGridItem(
                item = playlist,
                onClick = { onCLick(playlist) }
            )
        }
    }
}