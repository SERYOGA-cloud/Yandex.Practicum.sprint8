package com.example.playlistmaker.settings.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.playlistmaker.R
import com.example.playlistmaker.search.ui.composable.CustomTopAppBar
import com.example.playlistmaker.settings.ui.entity.SettingsItemType.ContactSupport
import com.example.playlistmaker.settings.ui.entity.SettingsItemType.Eula
import com.example.playlistmaker.settings.ui.entity.SettingsItemType.Sharing
import com.example.playlistmaker.settings.ui.entity.SettingsItemType.ThemeSwitcher
import com.example.playlistmaker.settings.ui.viewModel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    Scaffold(
        topBar = {
            CustomTopAppBar(stringResource(R.string.settings))
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { contentPadding ->
        val isDarkTheme = viewModel.observeIsDarkThemeEnabled().observeAsState()

        val onSwitchClicked = { checked: Boolean -> viewModel.onThemeSwitcherClicked(checked) }
        val onShareItemClicked = { viewModel.shareApp() }
        val onContactSupportItemClicked = { viewModel.openSupport() }
        val onEulaItemClicked = { viewModel.openTerms() }

        Column(
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Spacer(Modifier.height(dimensionResource(R.dimen.settings_header_bottom_spacer)))
            SettingsMenuItem(
                type = ThemeSwitcher,
                onItemClick = {},
                isDarkTheme = isDarkTheme.value ?: false,
                onCheckedChange = { onSwitchClicked(it) }
            )

            SettingsMenuItem(
                type = Sharing,
                onItemClick = { onShareItemClicked() }
            ) { }

            SettingsMenuItem(
                type = ContactSupport,
                onItemClick = { onContactSupportItemClicked() }
            ) { }

            SettingsMenuItem(
                type = Eula,
                onItemClick = { onEulaItemClicked() }
            ) { }
        }
    }
}