package com.example.playlistmaker.sharing.domain.api

interface StringResourceProvider {

    fun provideTermsLinkStringResource(): String

    fun provideShareLinkStringResource(): String

    fun provideSupportEmailStringResource(): String

    fun provideSupportMsgSubject(): String

    fun provideSupportMsgText(): String
}