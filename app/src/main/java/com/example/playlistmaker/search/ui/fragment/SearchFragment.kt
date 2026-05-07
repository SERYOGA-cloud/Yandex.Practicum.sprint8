package com.example.playlistmaker.search.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.ui.entity.SearchState
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel
import com.example.playlistmaker.util.ARGS_TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var binding: FragmentSearchBinding? = null
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var adapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrackAdapter(
            onItemClicked = { currentTrack ->

                viewModel.onItemClicked(currentTrack)

                findNavController().currentBackStackEntry?.savedStateHandle
                    ?.set(RETURNING_TO_SEARCH, true)


                findNavController().navigate(
                    R.id.action_global_to_playerFragment,
                    bundleOf(ARGS_TRACK to currentTrack)
                )
            },
            onItemLongClicked = {}
        )

        // Для принудительного показа результатов поиска после возврата с другого экрана
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Boolean>(RETURNING_TO_SEARCH)
            ?.observe(viewLifecycleOwner) { returning ->
                if (returning) {
                    viewModel.onReturnFromPlayer()

                    findNavController().currentBackStackEntry?.savedStateHandle?.set(
                        RETURNING_TO_SEARCH,
                        false
                    )
                }
            }

        viewModel.observeSearchState().observe(viewLifecycleOwner) {
            renderState(it)
        }

        binding!!.apply {
            searchContentLayout.content.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            searchContentLayout.content.adapter = adapter

            editTextSearch.doOnTextChanged { text, _, _, _ ->
                searchEditTextClearButton.isVisible = !text.isNullOrEmpty()
                viewModel.onQueryChanged(text.toString())
            }

            editTextSearch.setOnFocusChangeListener { _, hasFocus ->
                viewModel.onEditTextFocusChange(hasFocus)
            }

            searchEditTextClearButton.setOnClickListener {
                viewModel.onQueryChanged("")
                editTextSearch.text.clear()

                val inputMethodManager = requireActivity()
                    .getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

                val currentFocus = requireActivity().currentFocus
                if (currentFocus != null) {
                    inputMethodManager?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                }
                editTextSearch.clearFocus()
            }

            searchContentLayout.historyClearButton.setOnClickListener {
                viewModel.onClearHistoryButtonClicked()
                adapter.updateTrackList(emptyList())
            }

            searchPlaceholderLayout.tryAgainButton.setOnClickListener {
                viewModel.onTryAgainButtonClicked()
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun renderState(state: SearchState) {
        when (state) {
            is SearchState.Empty -> showEmpty()
            is SearchState.History -> showHistory(state)
            is SearchState.Loading -> showLoading()
            is SearchState.PlaceHolder -> showPlaceholder(state)
            is SearchState.Content -> showSearchResults(state)
        }
    }

    private fun showEmpty() {
        binding!!.apply {
            progressBar.visibility = View.GONE
            searchContentLayout.root.visibility = View.GONE
            searchPlaceholderLayout.root.visibility = View.GONE
        }
    }

    private fun showHistory(state: SearchState.History) {
        binding!!.apply {
            progressBar.visibility = View.GONE
            searchPlaceholderLayout.root.visibility = View.GONE
        }
        binding!!.searchContentLayout.apply {
            root.visibility = View.VISIBLE
            youSearchedText.visibility = View.VISIBLE
            historyClearButton.visibility = View.VISIBLE
        }
        adapter.updateTrackList(state.trackHistory)
    }

    private fun showLoading() {
        binding!!.apply {
            progressBar.visibility = View.VISIBLE
            searchContentLayout.root.visibility = View.GONE
            searchPlaceholderLayout.root.visibility = View.GONE
        }
    }

    private fun showPlaceholder(state: SearchState.PlaceHolder) {
        binding!!.apply {
            progressBar.visibility = View.GONE
            searchContentLayout.root.visibility = View.GONE
            searchPlaceholderLayout.root.visibility = View.VISIBLE
        }
        binding!!.searchPlaceholderLayout.apply {
            placeholderImage.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    state.imageId
                )
            )
            placeholderText.setText(state.textId)

            placeholderText.visibility = View.VISIBLE
            tryAgainButton.isVisible = state is SearchState.PlaceHolder.NetworkError
        }
    }

    private fun showSearchResults(state: SearchState.Content) {
        binding!!.apply {
            progressBar.visibility = View.GONE
            searchContentLayout.root.visibility = View.VISIBLE
            searchPlaceholderLayout.root.visibility = View.GONE
        }
        binding!!.searchContentLayout.apply {
            youSearchedText.visibility = View.GONE
            historyClearButton.visibility = View.GONE
        }
        adapter.updateTrackList(state.tracks)
    }

    companion object {
        // для отображения списка результатов поиска
        private const val RETURNING_TO_SEARCH = "returning_to_search"
    }
}