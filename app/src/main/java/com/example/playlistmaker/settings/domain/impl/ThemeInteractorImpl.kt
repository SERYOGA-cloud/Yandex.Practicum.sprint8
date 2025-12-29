package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.settings.domain.api.ThemeRepository

class ThemeInteractorImpl(private val repository: ThemeRepository) : ThemeInteractor {

    override fun switchTheme(isDarkThemeEnabled: Boolean) {
        repository.switchTheme(isDarkThemeEnabled)
    }

    override fun saveTheme(isDarkThemeEnabled: Boolean) {
        repository.saveTheme(isDarkThemeEnabled)
    }

    override fun isDarkThemeEnabled(): Boolean {
        return repository.isDarkThemeEnabled()
    }

    override fun isThemeSaved(): Boolean {
        return repository.isThemeSaved()
    }

    override fun applyThemeFromSaved() {
        if (!isThemeSaved()) {
            repository.applySystemTheme()
        } else {
            repository.applySavedTheme()
        }
    }
}
