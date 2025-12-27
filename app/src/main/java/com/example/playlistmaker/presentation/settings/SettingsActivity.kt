package com.example.playlistmaker.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.presentation.App
import com.example.playlistmaker.R
import com.example.playlistmaker.util.Creator
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeInteractor = Creator.provideThemeInteractor()

        val toolbar = findViewById<MaterialToolbar>(R.id.settings_toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switcher)

        themeSwitcher.isChecked = themeInteractor.getTheme()

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            recreate()
        }

        val shareButton = findViewById<ImageView>(R.id.shareButton)
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_dev_course_link))
            startActivity(shareIntent)
        }

        val supportButton = findViewById<ImageView>(R.id.supportButton)
        supportButton.setOnClickListener {
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

        val userAgreementButton = findViewById<ImageView>(R.id.userAgreementButton)
        userAgreementButton.setOnClickListener {
            val link = Uri.parse(getString(R.string.user_agreement_link))
            val userAgreementIntent = Intent(Intent.ACTION_VIEW, link)
            startActivity(userAgreementIntent)

        }

    }
}
