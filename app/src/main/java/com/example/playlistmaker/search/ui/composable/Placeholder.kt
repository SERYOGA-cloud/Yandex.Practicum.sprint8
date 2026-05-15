package com.example.playlistmaker.search.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ui.theme.LocalTypography

@Composable
fun Placeholder(
    textId: Int,
    imageId: Int,
    isNetworkError: Boolean = false,
    isPlaylists: Boolean = false,
    onReloadButtonClicked: () -> Unit = {}
) {
    val extraPadding = remember { if (isPlaylists) 0.dp else 80.dp }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier.padding(top = dimensionResource(R.dimen.search_placeholder_padding_top) + extraPadding),
            painter = painterResource(id = imageId),
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .padding(
                    top = dimensionResource(R.dimen.search_placeholder_text_padding_top),
                    bottom = dimensionResource(R.dimen.search_placeholder_text_padding_bottom)
                )
                .padding(horizontal = dimensionResource(R.dimen.search_placeholder_text_padding_horizontal)),
            text = stringResource(textId),
            style = LocalTypography.current.placeHolderText,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )

        if (isNetworkError) {
            val buttonText = stringResource(R.string.reload)
            CustomButton(
                text = buttonText,
                onClick = { onReloadButtonClicked() }
            )
        }
    }
}