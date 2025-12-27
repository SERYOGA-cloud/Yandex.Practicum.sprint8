package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.entity.PlayerState
import com.example.playlistmaker.util.Util.Companion.dpToPx
import com.example.playlistmaker.util.Util.Companion.getCoverArtwork512
import com.example.playlistmaker.util.Util.Companion.millisToMmSs

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private var previewUrl: String? = null
    private lateinit var audioPlayerInteractor: AudioPlayerInteractor

    private val handler = Handler(Looper.getMainLooper())

    private val timeRefreshRunnable = object : Runnable {

        override fun run() {
            binding.listeningTime.text = audioPlayerInteractor.getCurrentPositionConverted()
            handler.postDelayed(this, TIME_REFRESH_DELAY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playerToolbar.setNavigationOnClickListener { finish() }

        // Секция с информацией о треке
        binding.apply {
            trackName.text = intent.getStringExtra("track_name")
            artistName.text = intent.getStringExtra("artist_name")
            trackTime.text = millisToMmSs(intent.getIntExtra("track_time_converted", 0))
            yearValue.text = intent.getStringExtra("release_date")?.take(4)
            genre.text = intent.getStringExtra("primary_genre_name")
            country.text = intent.getStringExtra("country")
        }


        binding.listeningTime.text = millisToMmSs(LISTENING_TIME_DEFAULT)
        previewUrl = intent.getStringExtra("preview_url")

        try {
            audioPlayerInteractor = Creator.provideAudioPlayerInteractor(
                previewUrl = previewUrl,
                onStateChangedListener = { state ->
                    when (state) {
                        is PlayerState.PAUSED -> {
                            handler.removeCallbacks(timeRefreshRunnable)
                            binding.playButton.setImageResource(R.drawable.button_play)
                        }

                        is PlayerState.PLAYING -> {
                            handler.post(timeRefreshRunnable)
                            binding.playButton.setImageResource(R.drawable.button_pause)
                        }

                        is PlayerState.PREPARED -> {
                            handler.removeCallbacks(timeRefreshRunnable)
                            binding.listeningTime.text = millisToMmSs(LISTENING_TIME_DEFAULT)
                            binding.playButton.setImageResource(R.drawable.button_play)
                        }

                        is PlayerState.DEFAULT -> {}
                    }
                }
            )
        } catch (e: Exception) {
            showToast(e.message.toString())
        }

        val albumName = intent.getStringExtra("collection_name")
        if (albumName == null) {
            val albumNameText = findViewById<TextView>(R.id.album_text)
            albumNameText.isGone = true
            binding.albumName.isGone = true
        } else {
            binding.albumName.text = albumName
        }

        val albumCoverUrl100 = intent.getStringExtra("artwork_url_100")
        val artworkUrl512 = getCoverArtwork512(albumCoverUrl100)
        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.album_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8f, this)))
            .into(binding.albumCover)

        binding.playButton.setOnClickListener {
            try {
                audioPlayerInteractor.onPlayButtonClicked()
            } catch (e: Exception) {
                showToast(e.message.toString())
            }
        }
    }

    override fun onPause() {
        super.onPause()
        audioPlayerInteractor.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeRefreshRunnable)
        audioPlayerInteractor.releasePlayer()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val TIME_REFRESH_DELAY = 300L
        private const val LISTENING_TIME_DEFAULT = 0
    }
}