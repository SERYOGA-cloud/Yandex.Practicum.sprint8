package com.example.playlistmaker.settings.data.impl

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.example.playlistmaker.settings.domain.api.ThemeRepository

private const val DARK_THEME_ENABLED = "dark_theme_enabled"

class ThemeRepositoryImpl(
    private val prefs: SharedPreferences,
    private val app: Application
) : ThemeRepository {

    override fun switchTheme(isDarkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkThemeEnabled) MODE_NIGHT_YES else MODE_NIGHT_NO
        )
    }

    override fun saveTheme(isDarkThemeEnabled: Boolean) {
        prefs.edit()
            .putBoolean(DARK_THEME_ENABLED, isDarkThemeEnabled)
            .apply()
    }

    override fun isDarkThemeEnabled(): Boolean {
        return prefs.getBoolean(DARK_THEME_ENABLED, false)
    }

    override fun isThemeSaved(): Boolean {
        return prefs.contains(DARK_THEME_ENABLED)
    }

    override fun applySavedTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkThemeEnabled()) MODE_NIGHT_YES else MODE_NIGHT_NO
        )
    }

    override fun applySystemTheme() {

        val isSystemDarkThemeEnabled =
            (app.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        switchTheme(isSystemDarkThemeEnabled)
        saveTheme(isSystemDarkThemeEnabled)
    }
}