package com.example.playlistmaker.settings.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.app.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val themeInteractor = Creator.provideThemeInteractor()

        binding.settingsToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.themeSwitcher.isChecked = themeInteractor.getTheme()

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            recreate()
        }

        binding.shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_dev_course_link))
            startActivity(shareIntent)
        }

        binding.supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.support_email_address))
            )
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_text))
            startActivity(supportIntent)
        }

        binding.userAgreementButton.setOnClickListener {
            val link = Uri.parse(getString(R.string.user_agreement_link))
            val userAgreementIntent = Intent(Intent.ACTION_VIEW, link)
            startActivity(userAgreementIntent)

        }

    }
}
