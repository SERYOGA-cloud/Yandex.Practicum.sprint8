package com.example.playlistmaker.data.impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.ThemeRepository

private const val DARK_THEME_ENABLED = "dark_theme_enabled"

class ThemeRepositoryImpl(private val prefs: SharedPreferences) : ThemeRepository {
    override fun saveDarkThemeEnabled(enabled: Boolean) {
        prefs.edit()
            .putBoolean(DARK_THEME_ENABLED, enabled)
            .apply()
    }

    override fun isDarkThemeEnabled(): Boolean {
        return prefs.getBoolean(DARK_THEME_ENABLED, false)
    }

    override fun isThemeSaved(): Boolean {
        return prefs.contains(DARK_THEME_ENABLED)
    }
}
