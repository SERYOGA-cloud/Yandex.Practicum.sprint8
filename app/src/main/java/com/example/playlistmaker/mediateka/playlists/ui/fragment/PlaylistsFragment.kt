package com.example.playlistmaker.mediateka.playlists.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.mediateka.playlists.ui.entity.PlaylistsState
import com.example.playlistmaker.mediateka.playlists.ui.viewModel.PlaylistsViewModel
import com.example.playlistmaker.util.PLAYLISTS_GRID_COLUMNS_COUNT
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel: PlaylistsViewModel by viewModel()
    private var binding: FragmentPlaylistsBinding? = null

    private lateinit var adapter: PlaylistGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlaylistGridAdapter(
            onItemClicked = {
                //Здесь потом будет открываться экран плейлиста
            }
        )

        viewModel.observeState().observe(viewLifecycleOwner) {
            renderState(it)
        }

        binding!!.apply {
            playlistsContentLayout.apply {
                content.layoutManager =
                    GridLayoutManager(requireContext(), PLAYLISTS_GRID_COLUMNS_COUNT)
                content.adapter = adapter
            }

            newPlaylistButton.setOnClickListener {
                findNavController().navigate(R.id.action_global_to_newPlaylistFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun renderState(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Placeholder -> showPlaceholder()
            is PlaylistsState.Content -> showContent(state.playlists)
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        binding!!.apply {
            playlistsPlaceholderLayout.root.visibility = View.GONE
            playlistsContentLayout.root.visibility = View.VISIBLE
            adapter.updatePlaylists(playlists)
        }
    }

    private fun showPlaceholder() {
        binding!!.playlistsPlaceholderLayout.apply {
            root.visibility = View.VISIBLE
            placeholderImage.visibility = View.VISIBLE
            placeholderText.visibility = View.VISIBLE
            placeholderText.text = getString(R.string.you_havent_created_any_playlists)
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}