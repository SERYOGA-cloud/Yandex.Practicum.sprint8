package com.example.playlistmaker.playlist.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.mediateka.playlists.ui.fragment.NewPlaylistFragment
import com.example.playlistmaker.player.ui.fragment.PlayerFragment
import com.example.playlistmaker.playlist.ui.entity.PlaylistScreenState
import com.example.playlistmaker.playlist.ui.viewModel.PlaylistViewModel
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.search.ui.fragment.TrackAdapter
import com.example.playlistmaker.util.Util.Companion.dpToPx
import com.example.playlistmaker.util.Util.Companion.getRusNumeralMinutesEnding
import com.example.playlistmaker.util.Util.Companion.getRusNumeralTrackEnding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    private var binding: FragmentPlaylistBinding? = null
    private lateinit var viewModel: PlaylistViewModel
    private lateinit var adapter: TrackAdapter
    private lateinit var bottomSheetBehaviorContent: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorMenu: BottomSheetBehavior<LinearLayout>
    private lateinit var playlist: Playlist

    @Suppress("DEPRECATION")
    private val initialPlaylist by lazy {
        requireArguments().getParcelable<Playlist>(ARGS_PLAYLIST) as Playlist
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = getKoin().get { parametersOf(initialPlaylist) }
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlist = initialPlaylist

        fillInfoFromPlaylist()
        setupMenuBottomSheet()

        viewModel.observePlaylist().observe(viewLifecycleOwner) {
            this.playlist = it
            fillInfoFromPlaylist()
            setupMenuBottomSheet()
        }

        bottomSheetBehaviorContent = BottomSheetBehavior.from(binding!!.playlistBottomSheetContent)
        bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding!!.playlistMenuBottomSheet)

        bottomSheetBehaviorContent.apply {
            isHideable = false
            isFitToContents = false
        }

        bottomSheetBehaviorMenu.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STATE_HIDDEN -> {
                        binding?.playlistOverlay?.isGone = true
                    }

                    else -> {
                        binding?.playlistOverlay?.isVisible = true
                        binding?.playlistOverlay?.setOnClickListener {
                            viewModel.onOverlayClicked()
                        }
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        setupRecyclerView()
        setupViewModelObservers()
        setupButtonsListeners()
    }

    private fun setupButtonsListeners() {
        binding?.playlistToolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding?.playlistShareButton?.setOnClickListener {
            viewModel.onSharing()
        }

        binding?.playlistMenuButton?.setOnClickListener {
            viewModel.onMenuButtonClicked()
        }
    }

    private fun setupViewModelObservers() {
        viewModel.observeBottomSheetState().observe(viewLifecycleOwner) { state ->
            renderBottomSheetState(state)
        }
        viewModel.observeTracks().observe(viewLifecycleOwner) { tracks ->
            adapter.updateTrackList(tracks)
        }
        viewModel.observeShouldCloseScreen().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        viewModel.observeToastMessage().observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshPlaylist()
    }

    private fun renderBottomSheetState(state: PlaylistScreenState) {
        when (state) {
            PlaylistScreenState.Empty -> {
                hideContentBottomSheet()
                hideMenuBottomSheet()
                showPlaceholder(true)
            }

            PlaylistScreenState.ContentBottomSheet -> {
                showPlaceholder(false)
                showContentBottomSheet()
            }

            PlaylistScreenState.MenuBottomSheet -> {
                showPlaceholder(false)
                showMenuBottomSheet()
            }

            PlaylistScreenState.Sharing -> {
                showPlaceholder(false)
                hideMenuBottomSheet()
                hideContentBottomSheet()
            }
        }
    }

    private fun showContentBottomSheet() {
        binding?.root?.post {
            hideMenuBottomSheet()

            bottomSheetBehaviorContent.isHideable = false
            setupContentBottomSheetPeekHeight()
            bottomSheetBehaviorContent.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private fun showMenuBottomSheet() {
        hideContentBottomSheet()

        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HALF_EXPANDED

    }

    private fun hideContentBottomSheet() {
        bottomSheetBehaviorContent.isHideable = true
        bottomSheetBehaviorContent.state = STATE_HIDDEN
    }

    private fun hideMenuBottomSheet() {
        bottomSheetBehaviorMenu.state = STATE_HIDDEN
    }

    private fun setupMenuBottomSheet() {
        // Загрузка обложки и наполнение текста
        binding!!.playlistMenuPreview.apply {
            val trackCount = playlist.getTracksCount()
            val totalTracksText = requireContext().getString(
                R.string.tracks,
                trackCount,
                getRusNumeralTrackEnding(trackCount)
            )
            playlistMenuTitleMinified.text = playlist.title
            playlistMenuTracksCountMinified.text = totalTracksText

            Glide.with(root)
                .load(playlist.coverUri)
                .placeholder(R.drawable.album_placeholder)
                .transform(
                    CenterCrop(),
                    RoundedCorners(dpToPx(2f, root.context))
                )
                .into(playlistMenuCoverMinified)
        }
        setupMenuBottomSheetListeners()
    }

    private fun setupMenuBottomSheetListeners() {
        binding!!.apply {
            playlistBottomSheetShareButton.setOnClickListener {
                viewModel.onSharing()
            }

            playlistBottomSheetRedactButton.setOnClickListener {
                findNavController().navigate(
                    R.id.action_global_to_newPlaylistFragment,
                    bundleOf(NewPlaylistFragment.ARGS_PLAYLIST to playlist)
                )
            }

            playlistBottomSheetDeletePlaylistButton.setOnClickListener {
                showConfirmDeletePlaylistDialog()
            }
        }
    }

    private fun fillInfoFromPlaylist() {
        Glide.with(this)
            .load(playlist.coverUri)
            .placeholder(R.drawable.album_placeholder)
            .centerCrop()
            .into(binding!!.playlistCover)

        binding?.apply {
            playlistTitle.text = playlist.title

            if (playlist.description.isNullOrEmpty()) {
                playlistDescription.isGone = true
            } else {
                playlistDescription.isVisible = true
                playlistDescription.text = playlist.description
            }
        }
        updateTextFields()
    }

    private fun updateTextFields() {
        binding?.apply {
            val totalTimeMillis = playlist.getTotalTracksDuration()
            val totalTrackTimeText = getString(
                R.string.minutes,
                totalTimeMillis,
                getRusNumeralMinutesEnding(totalTimeMillis)
            )
            playlistTotalTrackTime.text = totalTrackTimeText

            val totalTracksCount = playlist.getTracksCount()
            val totalTracksCountText = getString(
                R.string.tracks,
                totalTracksCount,
                getRusNumeralTrackEnding(totalTracksCount)
            )
            playlistTracksCount.text = totalTracksCountText
        }
    }

    private fun setupRecyclerView() {
        adapter = TrackAdapter(
            onItemClicked = { currentTrack ->
                findNavController().navigate(
                    R.id.playerFragment,
                    bundleOf(PlayerFragment.ARGS_TRACK to currentTrack)
                )
            },
            onItemLongClicked = { currentTrack ->
                showConfirmDeleteTrackDialog(currentTrack)
            }
        )
        binding?.apply {
            playlistRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            playlistRecyclerView.adapter = adapter
        }
    }

    private fun showConfirmDeleteTrackDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomDialog)
            .setTitle(R.string.confirm_want_to_delete_track)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.onDeleteTrack(track)
            }
            .setNegativeButton(R.string.no) { _, _ -> }.show()
    }

    private fun showConfirmDeletePlaylistDialog() {
        val titleText = getString(R.string.confirm_want_to_delete_playlist, playlist.title)
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomDialog)
            .setTitle(titleText)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.onDeletePlaylist()
            }
            .setNegativeButton(R.string.no) { _, _ -> }.show()
    }

    // Вычисляем высоту для bottom_sheet, чтобы не перекрывались кнопки и тулбар
    private fun setupContentBottomSheetPeekHeight() {
        val totalScreenHeight = binding!!.root.height
        val toolbarHeight = binding!!.playlistToolbar.height
        val buttonsBottomPosition = binding!!.playlistShareButton.bottom

        val marginTop = resources.getDimensionPixelSize(R.dimen.playlist_bottom_sheet_margin_top)
        val peekHeight = totalScreenHeight - buttonsBottomPosition - marginTop

        bottomSheetBehaviorContent.peekHeight = peekHeight
        bottomSheetBehaviorContent.halfExpandedRatio = peekHeight.toFloat() / totalScreenHeight
        bottomSheetBehaviorContent.expandedOffset = toolbarHeight
    }

    private fun showPlaceholder(show: Boolean) {
        binding?.playlistPlaceholder?.apply {
            if (show) {
                root.isVisible = true
                placeholderText.setTextColor(getColor(requireContext(), R.color.yp_black))
                placeholderText.text = getString(R.string.no_tracks_added_to_playlist)
            } else {
                root.isGone = false
            }
        }

    }

    companion object {
        const val ARGS_PLAYLIST = "playlist"
    }
}