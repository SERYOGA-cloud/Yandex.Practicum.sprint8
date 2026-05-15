package com.example.playlistmaker.search.ui.composable

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ui.theme.LocalTypography
import com.example.playlistmaker.app.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.app.ui.theme.blue

@Composable
fun CustomSearchBar(
    initialQuery: String,
    onQueryChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val keyBoardController = LocalSoftwareKeyboardController.current
    var query by remember { mutableStateOf(initialQuery) }

    LaunchedEffect(isFocused) {
        onFocusChanged(isFocused)
    }

    BasicTextField(
        value = query,
        onValueChange = { newValue ->
            query = newValue
            onQueryChanged(newValue)
        },
        singleLine = true,
        textStyle = LocalTypography.current.searchText.copy(
            color = MaterialTheme.colorScheme.onTertiary
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        interactionSource = interactionSource,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), // без этого высота скачет при вводе запроса\отображении подсказки
        cursorBrush = SolidColor(blue),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(R.dimen.search_bar_padding_horizontal),
                        vertical = dimensionResource(R.dimen.search_bar_padding_vertical)
                    )
                    .background(
                        MaterialTheme.colorScheme.onPrimaryContainer,
                        RoundedCornerShape(8.dp)
                    )
                    .height(50.dp) // без этого высота скачет при вводе запроса\отображении подсказки
            ) {
                //иконка поиска
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.icon_search),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryFixedVariant,
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(R.dimen.search_icon_start_end_padding),
                            end = dimensionResource(R.dimen.search_icon_padding_to_text)
                        )
                        .padding(vertical = dimensionResource(R.dimen.search_icon_padding_vertical))
                )

                // Подсказка при пустом поле ввода
                Box(Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search),
                            style = LocalTypography.current.searchText.copy(
                                color = MaterialTheme.colorScheme.onPrimaryFixedVariant
                            )
                        )
                    }
                    innerTextField()
                }

                // Кнопка Х
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            query = ""
                            onQueryChanged(query)
                            focusManager.clearFocus()
                            keyBoardController?.hide()
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(
                                    end = dimensionResource(R.dimen.search_icon_start_end_padding),
                                    start = dimensionResource(R.dimen.search_icon_padding_to_text)
                                )
                                .padding(vertical = dimensionResource(R.dimen.search_icon_padding_vertical)),
                            imageVector = ImageVector.vectorResource(R.drawable.icon_x),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryFixedVariant
                        )
                    }
                }
            }
        }
    )
}


@Preview
@Composable
fun CustomSearchBarPreview() {
    PlaylistMakerTheme {
        CustomSearchBar(
            "",
            onQueryChanged = {},
            onFocusChanged = {},
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun CustomSearchBarNightPreview() {
    PlaylistMakerTheme {
        CustomSearchBar(
            "TEST",
            onQueryChanged = {},
            onFocusChanged = {},
        )
    }
}