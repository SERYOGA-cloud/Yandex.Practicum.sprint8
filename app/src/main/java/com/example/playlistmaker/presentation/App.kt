package com.example.playlistmaker.presentation

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.util.Creator

class App : Application() {

    private val themeInteractor: ThemeInteractor by lazy { Creator.provideThemeInteractor() }

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)

        if (!themeInteractor.isThemeSaved()) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            val isSystemDarkThemeEnabled =
                (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            darkTheme = isSystemDarkThemeEnabled
        } else {
            darkTheme = themeInteractor.getTheme()
            AppCompatDelegate.setDefaultNightMode(
                if (darkTheme) MODE_NIGHT_YES else MODE_NIGHT_NO
            )
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        themeInteractor.saveTheme(darkThemeEnabled)

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) MODE_NIGHT_YES else MODE_NIGHT_NO
        )
    }
}