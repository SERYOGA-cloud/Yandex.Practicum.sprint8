package com.example.playlistmaker.search.ui.composable

import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ui.theme.PlaylistMakerTheme

@Composable
fun CustomTopAppBar(
    text: String,
    isBackButtonEnabled: Boolean = false,
    navController: NavController? = null
) {
    if (isBackButtonEnabled) {
        TopAppBar(
            title = {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            backgroundColor = MaterialTheme.colorScheme.background,
            elevation = 0.dp,
            navigationIcon = {
                if (isBackButtonEnabled) {
                    IconButton(
                        onClick = { navController?.navigateUp() },
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.icon_arrow_back),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        )
    } else {
        TopAppBar(
            title = {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            backgroundColor = MaterialTheme.colorScheme.background,
            elevation = 0.dp
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun CustomTopAppBarNightPreview() {
    PlaylistMakerTheme {
        CustomTopAppBar("TEST test")
    }
}