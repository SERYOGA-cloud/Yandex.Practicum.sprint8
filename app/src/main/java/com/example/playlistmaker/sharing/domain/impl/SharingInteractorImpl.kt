package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.entity.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return SHARE_LINK
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            emailAddress = listOf(SUPPORT_EMAIL),
            subject = SUPPORT_MSG_SUBJECT,
            text = SUPPORT_MSG_TEXT
        )
    }

    private fun getTermsLink(): String {
        return TERMS_LINK
    }

    companion object {
        private const val SHARE_LINK = "https://practicum.yandex.ru/android-developer/"
        private const val TERMS_LINK = "https://yandex.ru/legal/practicum_offer/"

        private const val SUPPORT_EMAIL = "d-e-n-2010@mail.ru"
        private const val SUPPORT_MSG_SUBJECT =
            "Сообщение разработчикам и разработчицам приложения Playlist Maker"
        private const val SUPPORT_MSG_TEXT =
            "Спасибо разработчикам и разработчицам за крутое приложение!"
    }
}
