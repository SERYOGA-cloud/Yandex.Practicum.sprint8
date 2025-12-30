package com.example.playlistmaker.settings.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.sharing.domain.api.SharingInteractor


class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val themeInteractor: ThemeInteractor,
) : ViewModel() {

    private var isDarkThemeEnabledLiveData =
        MutableLiveData<Boolean>(themeInteractor.isDarkThemeEnabled())

    fun observeIsDarkThemeEnabled(): LiveData<Boolean> = isDarkThemeEnabledLiveData

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun onThemeSwitcherClicked(isChecked: Boolean) {
        isDarkThemeEnabledLiveData.value = isChecked
        themeInteractor.switchTheme(isChecked)
        themeInteractor.saveTheme(isChecked)
    }
}