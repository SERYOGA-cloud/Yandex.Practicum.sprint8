package com.example.playlistmaker.settings.domain.api

interface ThemeInteractor {

    fun saveTheme(isDarkThemeEnabled: Boolean)

    fun getTheme(): Boolean

    fun isThemeSaved(): Boolean
}