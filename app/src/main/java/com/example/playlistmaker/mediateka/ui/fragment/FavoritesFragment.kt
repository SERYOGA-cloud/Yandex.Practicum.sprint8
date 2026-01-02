package com.example.playlistmaker.mediateka.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.mediateka.ui.entity.FavoritesState
import com.example.playlistmaker.mediateka.ui.viewModel.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModel()
    private var binding: FragmentFavoritesBinding? = null

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
        viewModel.observeState().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun renderState(state: FavoritesState) {
        when (state) {
            is FavoritesState.Placeholder -> showPlaceholder()
        }
    }

    private fun showPlaceholder() {
        binding!!.placeholder.apply {
            placeholderButton.visibility = View.GONE
            placeholderImage.visibility = View.VISIBLE
            placeholderText.visibility = View.VISIBLE

            placeholderText.text = getString(R.string.your_mediateka_is_empty)
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}