package com.example.playlistmaker.mediateka.favorites.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.mediateka.favorites.ui.entity.FavoritesState
import com.example.playlistmaker.mediateka.favorites.ui.viewModel.FavoritesViewModel
import com.example.playlistmaker.player.ui.fragment.PlayerFragment
import com.example.playlistmaker.search.ui.fragment.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModel()
    private var binding: FragmentFavoritesBinding? = null
    private lateinit var adapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrackAdapter(
            onItemClicked = { currentTrack ->
                findNavController().navigate(
                    R.id.action_global_to_playerFragment,
                    bundleOf(PlayerFragment.ARGS_TRACK to currentTrack)
                )
            }
        )

        binding!!.favoritesContentLayout.apply {
            content.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            content.adapter = adapter
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavorites()
    }

    private fun renderState(state: FavoritesState) {
        when (state) {
            is FavoritesState.Placeholder -> showPlaceholder(state)
            is FavoritesState.Content -> showContent(state)
        }
    }

    private fun showContent(state: FavoritesState.Content) {
        binding!!.apply {
            favoritesContentLayout.root.visibility = View.VISIBLE
            favoritesContentLayout.content.visibility = View.VISIBLE
            favoritesPlaceholderLayout.root.visibility = View.GONE
            adapter.updateTrackList(state.tracks)
        }
    }

    private fun showPlaceholder(state: FavoritesState.Placeholder) {
        binding!!.apply {
            favoritesPlaceholderLayout.apply {
                root.visibility = View.VISIBLE
                placeholderImage.visibility = View.VISIBLE
                placeholderText.visibility = View.VISIBLE
                placeholderText.text = getString(state.textId)
            }
            favoritesContentLayout.root.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}