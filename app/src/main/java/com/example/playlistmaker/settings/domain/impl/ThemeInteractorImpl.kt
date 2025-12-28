package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.settings.domain.api.ThemeRepository

class ThemeInteractorImpl(private val repository: ThemeRepository) : ThemeInteractor {

    override fun saveTheme(isDarkThemeEnabled: Boolean) {
        repository.saveDarkThemeEnabled(isDarkThemeEnabled)
    }

    override fun getTheme(): Boolean {
        return repository.isDarkThemeEnabled()
    }

    override fun isThemeSaved(): Boolean {
        return repository.isThemeSaved()
    }
}
