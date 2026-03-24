package com.example.playlistmaker.sharing.domain.api

import com.example.playlistmaker.playlist.data.dto.PlaylistDto
import com.example.playlistmaker.sharing.domain.entity.EmailData

interface ExternalNavigator {

    fun shareLink(link: String)

    fun openLink(link: String)

    fun openEmail(emailData: EmailData)
    fun sharePlaylist(playlist: PlaylistDto)
}
