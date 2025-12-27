package com.example.playlistmaker.domain.api

interface ThemeRepository {

    fun saveDarkThemeEnabled(enabled: Boolean)

    fun isDarkThemeEnabled(): Boolean

    fun isThemeSaved(): Boolean
}