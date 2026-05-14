package com.example.playlistmaker.player.ui.fragment

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.player.services.PlayerService
import com.example.playlistmaker.player.ui.entity.PlayerScreenState
import com.example.playlistmaker.player.ui.viewModel.PlayerViewModel
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.util.ARGS_TRACK
import com.example.playlistmaker.util.Util.Companion.dpToPx
import com.example.playlistmaker.util.Util.Companion.getCoverArtwork512
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {

    private var binding: FragmentPlayerBinding? = null
    private val track: Track by lazy {
        @Suppress("DEPRECATION")
        requireArguments().getParcelable<Track>(ARGS_TRACK) as Track
    }
    private lateinit var viewModel: PlayerViewModel
    private lateinit var adapter: PlaylistLinearAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerServiceBinder
            viewModel.setAudioPlayerControl(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeAudioPlayerControl()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        viewModel.onPlayButtonClicked()
    }

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

        bindPlayerService()
        viewModel.initializeTrack(track)
        viewModel.updatePlaylists()

        binding!!.playerToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // region Секция с информацией о треке
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

        // endregion

        viewModel.observeScreenState().observe(viewLifecycleOwner) {
            renderState(it)
        }
        viewModel.observeToastMessage().observe(viewLifecycleOwner) {
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

        binding!!.playButton.onClickAction = {
            try {
                handlePlayButtonClick()
            } catch (e: Exception) {
                showToast(e.message.toString())
            }
        }

        binding!!.likeButton.setOnClickListener {
            viewModel.onFavoriteButtonClicked()
        }

        // region Все, что относится к нижней шторке
        bottomSheetBehavior = BottomSheetBehavior.from(binding!!.playerBottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding?.playerOverlay?.isVisible = true
                    }

                    else -> {
                        binding?.playerOverlay?.isGone = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        adapter = PlaylistLinearAdapter(
            onItemClicked = { playlist ->
                viewModel.addTrackToPlaylist(playlist)
            }
        )
        binding!!.apply {
            playerOverlay.setOnClickListener {
                viewModel.onOverlayClicked()
            }
            addToPlaylistButton.setOnClickListener {
                viewModel.onAddToPlaylistButtonClicked()
            }
            bottomSheetNewPlaylistButton.setOnClickListener {
                viewModel.onNewPlaylistButtonClicked()
                findNavController().navigate(R.id.action_global_to_newPlaylistFragment)
            }

            playerBottomSheetRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            playerBottomSheetRecyclerView.adapter = adapter
        }
        // endregion
    }

    override fun onPause() {
        super.onPause()
        viewModel.onAppMinimized()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onAppResumed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindPlayerService()
        binding = null
    }

    private fun handlePlayButtonClick() {
        // Android < 13
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            viewModel.onPlayButtonClicked()
            return
        }

        // Android >= 13
        if (isPermissionToPostNotificationsGranted()) {
            viewModel.onPlayButtonClicked()
        } else {
            requestPostNotificationsPermission()
        }

    }

    private fun requestPostNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun isPermissionToPostNotificationsGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PERMISSION_GRANTED
        } else {
            true
        }
    }


    private fun renderState(state: PlayerScreenState) {
            binding!!.apply {
            showPlaying(state)
                showFavoriteButtonActive(state.isFavorite)
                listeningTime.text = state.currentPosition
                showBottomSheet(state.isBottomSheetVisible)
                adapter.updatePlaylists(state.playlists)
            }
    }

    private fun showBottomSheet(show: Boolean) {
        if (show) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun showPlaying(state: PlayerScreenState) {
        binding!!.apply {
            if (state.isPlayButtonShown) {
                playButton.showPlayButton()
            } else {
                playButton.showPauseButton()
            }
        }
    }

    private fun showFavoriteButtonActive(isActive: Boolean) {
        if (isActive) {
            binding!!.likeButton.setImageResource(R.drawable.button_like_active)
        } else {
            binding!!.likeButton.setImageResource(R.drawable.button_like_inactive)
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    private fun bindPlayerService() {
        requireContext().bindService(
            getPlayerServiceIntent(),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun unbindPlayerService() {
        requireContext().unbindService(serviceConnection)
    }

    private fun getPlayerServiceIntent(): Intent {
        return Intent(requireContext(), PlayerService::class.java).apply {
            putExtra(ARGS_TRACK, track)
        }
    }
}