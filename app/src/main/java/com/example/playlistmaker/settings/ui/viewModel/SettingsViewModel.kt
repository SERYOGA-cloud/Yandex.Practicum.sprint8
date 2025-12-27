package com.example.playlistmaker.settings.ui.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.app.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.api.ThemeInteractor
import com.example.playlistmaker.sharing.domain.api.SharingInteractor


class SettingsViewModel(
    private val appContext: Application,
    private val sharingInteractor: SharingInteractor,
    private val themeInteractor: ThemeInteractor,
) : ViewModel() {

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                SettingsViewModel(
                    app,
                    Creator.provideSharingInteractor(),
                    Creator.provideThemeInteractor()
                )
            }
        }
    }

    private var isDarkThemeEnabledLiveData = MutableLiveData<Boolean>(themeInteractor.getTheme())
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

    fun switchTheme(darkThemeEnabled: Boolean) {
        (appContext as App).switchTheme(darkThemeEnabled)
        isDarkThemeEnabledLiveData.postValue(darkThemeEnabled)
        themeInteractor.saveTheme(darkThemeEnabled)
    }
}