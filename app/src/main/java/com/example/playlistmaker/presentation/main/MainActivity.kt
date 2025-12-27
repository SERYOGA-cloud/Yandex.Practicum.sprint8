package com.example.playlistmaker.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.mediateka.MediatekaActivity
import com.example.playlistmaker.presentation.search.SearchActivity
import com.example.playlistmaker.presentation.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.mainMenuSearchButton)
        searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        val mediaButton = findViewById<Button>(R.id.mainMenuMediaButton)
        val mediaClickListener = object : OnClickListener {
            override fun onClick(v: View?) {
                val mediaIntent = Intent(this@MainActivity, MediatekaActivity::class.java)
                startActivity(mediaIntent)
            }
        }
        mediaButton.setOnClickListener(mediaClickListener)

        val settingsButton = findViewById<Button>(R.id.mainMenuSettingsButton)
        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}
