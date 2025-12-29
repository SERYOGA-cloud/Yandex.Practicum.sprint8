package com.example.playlistmaker.settings.domain.api

interface ThemeRepository {

    fun switchTheme(isDarkThemeEnabled: Boolean)

    fun saveTheme(isDarkThemeEnabled: Boolean)

    fun isDarkThemeEnabled(): Boolean

    fun isThemeSaved(): Boolean

    fun applySystemTheme()

    fun applySavedTheme()
}