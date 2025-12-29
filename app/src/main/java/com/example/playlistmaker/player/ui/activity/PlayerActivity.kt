package com.example.playlistmaker.player.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.entity.PlayerState
import com.example.playlistmaker.player.ui.entity.PlayerScreenState
import com.example.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.util.Util.Companion.dpToPx
import com.example.playlistmaker.util.Util.Companion.getCoverArtwork512

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var viewModel: PlayerViewModel

    private var previewUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getParcelableExtra<Track>("track")

        previewUrl = track?.previewUrl

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getFactory(previewUrl)
        )[PlayerViewModel::class.java]

        binding.playerToolbar.setNavigationOnClickListener { finish() }

        // Секция с информацией о треке
        binding.apply {
            trackName.text = track?.trackName
            artistName.text = track?.artistName
            trackTime.text = track?.trackTimeConverted
            yearValue.text = track?.releaseDate?.take(4).toString()
            genre.text = track?.primaryGenreName
            country.text = track?.country
        }

        viewModel.observeScreenState().observe(this) {
            when (it.getPlayerState()) {
                PlayerState.DEFAULT -> showPreparedDefaultOrPaused(it)
                PlayerState.PAUSED -> showPreparedDefaultOrPaused(it)
                PlayerState.PLAYING -> showPlaying(it)
                PlayerState.PREPARED -> showPreparedDefaultOrPaused(it)
            }
        }

        viewModel.observeErrorMessage().observe(this) {
            showToast(it)
        }

        val albumName = track?.collectionName
        if (albumName == null) {
            binding.albumName.isGone = true
            binding.albumName.isGone = true
        } else {
            binding.albumName.text = albumName
        }

        val albumCoverUrl100 = track?.artworkUrl100
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

    private fun showPreparedDefaultOrPaused(state: PlayerScreenState) {
        binding.playButton.setImageResource(R.drawable.button_play)
        binding.listeningTime.text = state.getCurrentPosition()
    }

    private fun showPlaying(state:PlayerScreenState) {
        binding.playButton.setImageResource(R.drawable.button_pause)
        binding.listeningTime.text = state.getCurrentPosition()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
