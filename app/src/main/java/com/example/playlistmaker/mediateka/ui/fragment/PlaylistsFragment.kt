package com.example.playlistmaker.mediateka.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.mediateka.ui.entity.PlaylistsState
import com.example.playlistmaker.mediateka.ui.viewModel.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel: PlaylistsViewModel by viewModel()
    private var binding: FragmentPlaylistsBinding? = null

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
        viewModel.observeState().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun renderState(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Placeholder -> showPlaceholder()
        }
    }

    private fun showPlaceholder() {
        binding!!.placeholderLayoutFavorites.apply {
            root.visibility = View.VISIBLE
            placeholderButton.visibility = View.VISIBLE
            placeholderImage.visibility = View.VISIBLE
            placeholderText.visibility = View.VISIBLE

            placeholderText.text = getString(R.string.you_havent_created_any_playlists)
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}