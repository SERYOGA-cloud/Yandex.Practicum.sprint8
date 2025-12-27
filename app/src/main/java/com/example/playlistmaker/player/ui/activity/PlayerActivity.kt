package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.entity.PlayerState
import com.example.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.example.playlistmaker.util.Util.Companion.dpToPx
import com.example.playlistmaker.util.Util.Companion.getCoverArtwork512
import com.example.playlistmaker.util.Util.Companion.millisToMmSs

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var viewModel: PlayerViewModel

    private var previewUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        previewUrl = intent.getStringExtra("preview_url")

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getFactory(previewUrl)
        )[PlayerViewModel::class.java]

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

        viewModel.observePlayerState().observe(this) {
            when (it) {
                PlayerState.DEFAULT -> {}
                PlayerState.PAUSED -> {
                    binding.playButton.setImageResource(R.drawable.button_play)
                }

                PlayerState.PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.button_pause)
                }

                PlayerState.PREPARED -> {
                    binding.playButton.setImageResource(R.drawable.button_play)
                }
            }
        }

        viewModel.observeProgressTime().observe(this) {
            binding.listeningTime.text = it
        }

        viewModel.observeErrorMessage().observe(this) {
            showToast(it)
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
                viewModel.onPlayButtonClicked()
            } catch (e: Exception) {
                showToast(e.message.toString())
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
