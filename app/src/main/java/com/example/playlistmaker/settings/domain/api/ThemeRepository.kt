package com.example.playlistmaker.settings.domain.api

interface ThemeRepository {

    fun saveDarkThemeEnabled(enabled: Boolean)

    fun isDarkThemeEnabled(): Boolean

    fun isThemeSaved(): Boolean
}