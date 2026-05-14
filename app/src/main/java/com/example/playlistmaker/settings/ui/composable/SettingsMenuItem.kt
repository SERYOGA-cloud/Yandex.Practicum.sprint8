package com.example.playlistmaker.settings.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ui.theme.LocalTypography
import com.example.playlistmaker.app.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.settings.ui.entity.SettingsItemType

@Composable
fun SettingsMenuItem(
    type: SettingsItemType,
    onItemClick: () -> Unit,
    isDarkTheme: Boolean = false,
    onCheckedChange: (checked: Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(R.dimen.settings_menu_item_padding_start),
                end = dimensionResource(R.dimen.settings_menu_item_padding_end)

            )
            .padding(vertical = dimensionResource(R.dimen.settings_menu_item_padding_vertical))
            .clickable { onItemClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(type.textId),
            style = LocalTypography.current.settingsMenuItemText,
            color = MaterialTheme.colorScheme.onPrimary
        )

        when (type) {

            SettingsItemType.ThemeSwitcher -> {
                CustomSwitch(
                    checked = isDarkTheme,
                    onCheckedChange = { onCheckedChange(it) }
                )
            }

            else -> {
                Icon(
                    imageVector = ImageVector.vectorResource(type.iconId!!),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryFixed
                )
            }
        }
    }
}

@Preview
@Composable
fun SettingMenuItemPreview() {
    val type = SettingsItemType.ThemeSwitcher
    PlaylistMakerTheme {
        SettingsMenuItem(
            type, {},
            isDarkTheme = true,
            onCheckedChange = { },
        )
    }
}