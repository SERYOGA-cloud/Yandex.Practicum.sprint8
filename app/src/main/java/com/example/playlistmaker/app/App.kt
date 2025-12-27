package com.example.playlistmaker.app

import android.app.Application
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.api.ThemeInteractor

class App : Application() {

    private val themeInteractor: ThemeInteractor by lazy { Creator.provideThemeInteractor() }

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)

        themeInteractor.applyThemeFromSaved()
    }
}
