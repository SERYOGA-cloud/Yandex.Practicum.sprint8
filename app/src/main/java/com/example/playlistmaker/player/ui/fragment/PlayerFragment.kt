package com.example.playlistmaker.player.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.player.domain.entity.PlayerState
import com.example.playlistmaker.player.ui.entity.PlayerScreenState
import com.example.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.util.Util.Companion.dpToPx
import com.example.playlistmaker.util.Util.Companion.getCoverArtwork512
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {

    private var binding: FragmentPlayerBinding? = null
    private val track: Track by lazy {
        @Suppress("DEPRECATION")
        requireArguments().getParcelable<Track>(ARGS_TRACK) as Track
    }
    private lateinit var viewModel: PlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = getKoin().get { parametersOf(track.previewUrl) }
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.playerToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()

        }

        // Секция с информацией о треке
        binding!!.apply {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = track.trackTimeConverted
            yearValue.text = track.releaseDate.take(4)
            genre.text = track.primaryGenreName
            country.text = track.country
        }

        val albumName = track.collectionName
        if (albumName == null) {
            binding!!.albumName.isGone = true
            binding!!.albumText.isGone = true
        } else {
            binding!!.albumName.text = albumName
        }

        viewModel.observeScreenState().observe(viewLifecycleOwner) {
            when (it.getPlayerState()) {
                PlayerState.DEFAULT -> showPreparedDefaultOrPaused(it)
                PlayerState.PAUSED -> showPreparedDefaultOrPaused(it)
                PlayerState.PLAYING -> showPlaying(it)
                PlayerState.PREPARED -> showPreparedDefaultOrPaused(it)
            }
        }
        viewModel.observeErrorMessage().observe(viewLifecycleOwner) {
            showToast(it)
        }

        val albumCoverUrl100 = track.artworkUrl100
        val artworkUrl512 = getCoverArtwork512(albumCoverUrl100)
        Glide.with(this)
            .load(artworkUrl512)
            .placeholder(R.drawable.album_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8f, requireContext())))
            .into(binding!!.albumCover)

        binding!!.playButton.setOnClickListener {
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun showPreparedDefaultOrPaused(state: PlayerScreenState) {
        binding!!.apply {
            playButton.setImageResource(R.drawable.button_play)
            listeningTime.text = state.getCurrentPosition()
        }
    }

    private fun showPlaying(state: PlayerScreenState) {
        binding!!.apply {
            playButton.setImageResource(R.drawable.button_pause)
            listeningTime.text = state.getCurrentPosition()
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val ARGS_TRACK = "track"
    }
}