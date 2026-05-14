package com.example.playlistmaker.search.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ui.theme.LocalTypography
import com.example.playlistmaker.app.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.util.Debouncer

@Composable
fun TrackList(
    items: List<Track>,
    onItemCLicked: (item: Track) -> Unit,
    isHistory: Boolean = false,
    onClearHistoryClicked: () -> Unit = {}
) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        if (isHistory) {
            Spacer(Modifier.height(dimensionResource(R.dimen.you_searched_top_spacer)))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.track_history_header_padding_horizontal))
                    .padding(
                        top = dimensionResource(R.dimen.track_history_header_padding_top),
                        bottom = dimensionResource(R.dimen.track_history_header_padding_bottom)
                    ),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.you_searched),
                style = LocalTypography.current.placeHolderText
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.you_searched_bottom_spacer)))
        }

        LazyColumn {
            items(items) { item ->
                TrackItem(
                    item = item,
                    onClick = { onItemCLicked(item) }
                )
            }
        }

        if (isHistory) {
            Spacer(Modifier.height(dimensionResource(R.dimen.clear_history_button_top_spacer)))

            CustomButton(
                text = stringResource(R.string.clear_history),
                onClick = { onClearHistoryClicked() }
            )
        }
    }
}

@Composable
fun TrackItem(
    item: Track,
    onClick: (track: Track) -> Unit
) {
    val debouncer = remember { Debouncer() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (debouncer.clickDebounce()) {
                    onClick(item)
                }
            }
            .padding(
                start = dimensionResource(R.dimen.search_result_view_padding_left),
                end = dimensionResource(R.dimen.search_result_view_padding_right)
            )
            .padding(vertical = dimensionResource(R.dimen.track_item_padding_vertical)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // Обложка альбома
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.artworkUrl100)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .clip(RoundedCornerShape(dimensionResource(R.dimen.list_item_image_radius)))
                .padding(end = dimensionResource(R.dimen.search_result_album_cover_padding)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.album_placeholder)
        )

        // Контейнер для текста
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Название трека. Верхняя строка
            Text(
                text = item.trackName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                style = LocalTypography.current.trackListUpperText,
                color = MaterialTheme.colorScheme.onPrimary
            )

            // Контейнер для нижней строки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.dp)
            ) {
                // Название группы/ число треков плейлиста
                Text(
                    text = item.artistName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = LocalTypography.current.trackListLowerText,
                    color = MaterialTheme.colorScheme.onPrimaryFixed
                )

                //Разделитель текста (точка)
                Icon(
                    painterResource(R.drawable.text_divider_dot),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    tint = MaterialTheme.colorScheme.onPrimaryFixed
                )

                //Продолжительность трека
                Text(
                    text = item.trackTimeConverted,
                    maxLines = 1,
                    style = LocalTypography.current.trackListLowerText,
                    color = MaterialTheme.colorScheme.onPrimaryFixed
                )
            }
        }
        // Иконка ">"
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.icon_arrow_forward),
            contentDescription = null,
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.search_icon_padding_to_text)),
            tint = MaterialTheme.colorScheme.onPrimaryFixed
        )
    }
}

@Preview
@Composable
fun TrackItemPreview() {
    PlaylistMakerTheme {
        TrackItem(mockTrack) {}
    }
}

private val mockTrack: Track = Track(
    trackId = 1,
    trackName = "Billie Jean",
    artistName = "Michael Jackson",
    collectionName = null,
    releaseDate = "",
    primaryGenreName = null,
    country = "",
    trackTimeMillis = 0,
    trackTimeConverted = "4:35",
    artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg",
    previewUrl = null,
    isFavorite = false
)