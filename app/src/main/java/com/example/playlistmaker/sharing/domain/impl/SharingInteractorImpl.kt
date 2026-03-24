package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.mediateka.playlists.domain.converter.PlaylistConverter
import com.example.playlistmaker.mediateka.playlists.domain.converter.PlaylistConverter.toPlaylistDto
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.api.StringResourceProvider
import com.example.playlistmaker.sharing.domain.entity.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val stringResourceProvider: StringResourceProvider
) : SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun sharePlaylist(playlist: Playlist) {
        externalNavigator.sharePlaylist(toPlaylistDto(playlist))
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return stringResourceProvider.provideShareLinkStringResource()
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            emailAddress = listOf(stringResourceProvider.provideSupportEmailStringResource()),
            subject = stringResourceProvider.provideSupportMsgSubject(),
            text = stringResourceProvider.provideSupportMsgText()
        )
    }

    private fun getTermsLink(): String {
        return stringResourceProvider.provideTermsLinkStringResource()
    }
}
