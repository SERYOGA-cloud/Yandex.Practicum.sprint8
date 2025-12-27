package com.example.playlistmaker.presentation.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.entity.PlayerState
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.util.Util.Companion.dpToPx
import com.example.playlistmaker.util.Util.Companion.getCoverArtwork512
import com.example.playlistmaker.util.Util.Companion.millisToMmSs
import com.google.android.material.appbar.MaterialToolbar

class PlayerActivity : AppCompatActivity() {

    private var previewUrl: String? = null
    private lateinit var playButton: ImageButton
    private lateinit var listeningTimeText: TextView
    private lateinit var audioPlayerInteractor: AudioPlayerInteractor

    private val handler = Handler(Looper.getMainLooper())

    private val timeRefreshRunnable = object : Runnable {

        override fun run() {
            listeningTimeText.text = audioPlayerInteractor.getCurrentPositionConverted()
            handler.postDelayed(this, TIME_REFRESH_DELAY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val toolbar: MaterialToolbar = findViewById(R.id.player_toolbar)
        listeningTimeText = findViewById(R.id.listening_time)
        playButton = findViewById(R.id.play_button)

        val albumCover: ImageView = findViewById(R.id.album_cover)
        val trackName: TextView = findViewById(R.id.track_name)
        val artistName: TextView = findViewById(R.id.artist_name)
        val trackTime: TextView = findViewById(R.id.track_time)
        val collectionName: TextView = findViewById(R.id.album_name)
        val releaseDate: TextView = findViewById(R.id.year_value)
        val primaryGenreName: TextView = findViewById(R.id.genre)
        val country: TextView = findViewById(R.id.country)

        toolbar.setNavigationOnClickListener { finish() }

        // Секция с информацией о треке
        trackName.text = intent.getStringExtra("track_name")
        artistName.text = intent.getStringExtra("artist_name")
        trackTime.text = millisToMmSs(intent.getIntExtra("track_time_converted", 0))
        releaseDate.text = intent.getStringExtra("release_date")?.take(4)
        primaryGenreName.text = intent.getStringExtra("primary_genre_name")
        country.text = intent.getStringExtra("country")

        listeningTimeText.text = millisToMmSs(LISTENING_TIME_DEFAULT)
        previewUrl = intent.getStringExtra("preview_url")

        try {
            audioPlayerInteractor = Creator.provideAudioPlayerInteractor(
                previewUrl = previewUrl,
                onStateChangedListener = { state ->
                    when (state) {
                        is PlayerState.PAUSED -> {
                            handler.removeCallbacks(timeRefreshRunnable)
                            playButton.setImageResource(R.drawable.button_play)
                        }

                        is PlayerState.PLAYING -> {
                            handler.post(timeRefreshRunnable)
                            playButton.setImageResource(R.drawable.button_pause)
                        }

                        is PlayerState.PREPARED -> {
                            handler.removeCallbacks(timeRefreshRunnable)
                            listeningTimeText.text = millisToMmSs(LISTENING_TIME_DEFAULT)
                            playButton.setImageResource(R.drawable.button_play)
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
            collectionName.isGone = true
        } else {
            collectionName.text = albumName
        }

        val albumCoverUrl100 = intent.getStringExtra("artwork_url_100")
        val artworkUrl512 = getCoverArtwork512(albumCoverUrl100)
        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.album_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8f, this)))
            .into(albumCover)

        playButton.setOnClickListener {
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