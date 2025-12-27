package com.example.playlistmaker.mediateka.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMediatekaBinding

class MediatekaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}