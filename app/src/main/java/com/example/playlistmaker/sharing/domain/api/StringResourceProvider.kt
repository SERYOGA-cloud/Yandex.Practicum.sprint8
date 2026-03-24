package com.example.playlistmaker.sharing.domain.api

interface StringResourceProvider {

    fun provideTermsLinkStringResource(): String

    fun provideShareLinkStringResource(): String

    fun provideSupportEmailStringResource(): String

    fun provideSupportMsgSubject(): String

    fun provideSupportMsgText(): String

    fun getPlaylistCreatedMsg(playlistTitle: String): String

    fun getTrackAlreadyAddedMsg(playlistName: String): String

    fun getTrackAddedSuccessfullyMsg(playlistName: String): String
    fun getNoTracksInPlaylistToShareMsg(): String
    fun getNoChangesInPlaylistMadeMsg(): String
}