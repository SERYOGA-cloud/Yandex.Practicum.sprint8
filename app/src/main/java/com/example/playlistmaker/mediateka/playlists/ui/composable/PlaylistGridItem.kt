package com.example.playlistmaker.mediateka.playlists.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ui.theme.LocalTypography
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.util.Debouncer
import com.example.playlistmaker.util.Util.Companion.getRusNumeralTrackEnding


@Composable
fun PlaylistGridItem(item: Playlist, onClick: (playlist: Playlist) -> Unit) {

    val context = LocalContext.current

    val tracksCount = item.tracks?.size ?: 0
    val totalTracksCountText = context.getString(
        R.string.tracks,
        tracksCount,
        getRusNumeralTrackEnding(tracksCount)
    )

    val debouncer = remember { Debouncer() }

    Column(Modifier.clickable {
        if (debouncer.clickDebounce()) {
            onClick(item)
        }
    }) {

        // Обложка альбома
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.coverUri)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.grid_item_image_padding))
                .aspectRatio(1f)
                .clip(RoundedCornerShape(dimensionResource(R.dimen.grid_item_image_radius))),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.album_placeholder),
            error = painterResource(R.drawable.album_placeholder)
        )

        Column(
            Modifier
                .padding(horizontal = dimensionResource(R.dimen.grid_item_image_padding))
                .padding(bottom = dimensionResource(R.dimen.grid_item_padding_bottom))
        ) {

            // Название альбома
            Text(
                text = item.title,
                style = LocalTypography.current.gridItemText,
                color = MaterialTheme.colorScheme.onPrimary
            )

            // Количество треков
            Text(
                text = totalTracksCountText,
                style = LocalTypography.current.gridItemText,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}