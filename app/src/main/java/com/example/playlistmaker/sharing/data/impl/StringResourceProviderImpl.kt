package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.api.StringResourceProvider

class StringResourceProviderImpl(private val context: Context) : StringResourceProvider {
    override fun provideTermsLinkStringResource(): String {
        return context.getString(R.string.user_agreement_link)
    }

    override fun provideShareLinkStringResource(): String {
        return context.getString(R.string.android_dev_course_link)
    }

    override fun provideSupportEmailStringResource(): String {
        return context.getString(R.string.support_email_address)
    }

    override fun provideSupportMsgSubject(): String {
        return context.getString(R.string.support_email_subject)
    }

    override fun provideSupportMsgText(): String {
        return context.getString(R.string.support_email_text)
    }

}