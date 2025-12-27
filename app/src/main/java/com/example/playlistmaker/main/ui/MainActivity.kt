package com.example.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.mediateka.ui.activity.MediatekaActivity
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.example.playlistmaker.settings.ui.activity.SettingsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchButton = binding.mainMenuSearchButton
        searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        val mediaButton = binding.mainMenuMediaButton
        val mediaClickListener = object : OnClickListener {
            override fun onClick(v: View?) {
                val mediaIntent = Intent(this@MainActivity, MediatekaActivity::class.java)
                startActivity(mediaIntent)
            }
        }
        mediaButton.setOnClickListener(mediaClickListener)

        val settingsButton = binding.mainMenuSettingsButton
        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}
