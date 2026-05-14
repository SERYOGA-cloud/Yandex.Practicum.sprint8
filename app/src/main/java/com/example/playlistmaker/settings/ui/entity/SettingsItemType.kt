package com.example.playlistmaker.settings.ui.entity

import com.example.playlistmaker.R

sealed class SettingsItemType(val textId: Int, val iconId: Int?) {

    data object ThemeSwitcher : SettingsItemType(textId = R.string.dark_theme, iconId = null)

    data object Sharing : SettingsItemType(R.string.share_app, R.drawable.ic_share)

    data object ContactSupport : SettingsItemType(R.string.text_support, R.drawable.icon_support)

    data object Eula : SettingsItemType(R.string.user_agreement, R.drawable.icon_arrow_forward)
}