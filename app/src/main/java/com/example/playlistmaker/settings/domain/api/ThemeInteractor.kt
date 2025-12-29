package com.example.playlistmaker.settings.domain.api

interface ThemeInteractor {

    fun switchTheme(isDarkThemeEnabled: Boolean)

    fun saveTheme(isDarkThemeEnabled: Boolean)

    fun isDarkThemeEnabled(): Boolean

    fun isThemeSaved(): Boolean

    fun applyThemeFromSaved()
}