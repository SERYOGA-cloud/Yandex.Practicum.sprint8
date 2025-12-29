package com.example.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.ui.entity.SearchState
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private var searchSavedInput: String = INPUT_DEF

    private lateinit var adapter: TrackAdapter
    private val viewModel: SearchViewModel by viewModels()

    private lateinit var binding: ActivitySearchBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        viewModel.observeSearchState().observe(this) {
            renderState(it)
        }

        binding.recyclerViewLayout.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = TrackAdapter(
            onItemClicked = { currentTrack ->
                viewModel.onItemClicked(currentTrack)
                val playerIntent =
                    Intent(this, PlayerActivity::class.java)
                        .putExtra("track", currentTrack)

                startActivity(playerIntent)
            }
        )
        binding.recyclerViewLayout.recyclerView.adapter = adapter

        binding.searchToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.editTextSearch.doOnTextChanged { text, _, _, _ ->
            binding.searchEditTextClearButton.isVisible = !text.isNullOrEmpty()
            viewModel.onQueryChanged(text.toString())
        }

        binding.editTextSearch.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onEditTextFocusChange(hasFocus)
        }

        binding.searchEditTextClearButton.setOnClickListener {
            viewModel.onQueryChanged("")
            binding.editTextSearch.text.clear()

            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            if (currentFocus != null) {
                inputMethodManager?.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
            binding.editTextSearch.clearFocus()
        }

        binding.recyclerViewLayout.historyClearButton.setOnClickListener {
            viewModel.onClearHistoryButtonClicked()
            adapter.updateTrackList(emptyList())
        }

        binding.placeholderLayout.tryAgainButton.setOnClickListener {
            viewModel.onTryAgainButtonClicked()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_INPUT, searchSavedInput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchSavedInput = savedInstanceState.getString(SAVED_INPUT, INPUT_DEF)

        if (searchSavedInput.isNotEmpty()) {
            binding.editTextSearch.setText(searchSavedInput)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private fun renderState(state: SearchState) {
        when (state) {
            is SearchState.Empty -> showEmpty()
            is SearchState.History -> showHistory(state)
            is SearchState.Loading -> showLoading()
            is SearchState.PlaceHolder -> showPlaceholder(state)
            is SearchState.SearchResults -> showSearchResults(state)
        }
    }

    private fun showEmpty() {
        binding.apply {
            progressBar.visibility = View.GONE
            recyclerViewLayout.root.visibility = View.GONE
            placeholderLayout.root.visibility = View.GONE
        }
    }

    private fun showHistory(state: SearchState.History) {
        binding.apply {
            progressBar.visibility = View.GONE
            placeholderLayout.root.visibility = View.GONE
        }
        binding.recyclerViewLayout.apply {
            root.visibility = View.VISIBLE
            youSearchedText.visibility = View.VISIBLE
            historyClearButton.visibility = View.VISIBLE
        }
        adapter.updateTrackList(state.trackHistory)
    }

    private fun showLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            recyclerViewLayout.root.visibility = View.GONE
            placeholderLayout.root.visibility = View.GONE
        }
    }

    private fun showPlaceholder(state: SearchState.PlaceHolder) {
        binding.apply {
            progressBar.visibility = View.GONE
            recyclerViewLayout.root.visibility = View.GONE
            placeholderLayout.root.visibility = View.VISIBLE
        }
        binding.placeholderLayout.apply {
            placeholderImage.setImageDrawable(
                AppCompatResources.getDrawable(
                    this@SearchActivity,
                    state.imageId
                )
            )
            placeholderText.setText(state.textId)

            placeholderText.visibility = View.VISIBLE
            tryAgainButton.isVisible = state is SearchState.PlaceHolder.NetworkError
        }
    }

    private fun showSearchResults(state: SearchState.SearchResults) {
        binding.apply {
            progressBar.visibility = View.GONE
            recyclerViewLayout.root.visibility = View.VISIBLE
            placeholderLayout.root.visibility = View.GONE
        }
        binding.recyclerViewLayout.apply {
            youSearchedText.visibility = View.GONE
            historyClearButton.visibility = View.GONE
        }
        adapter.updateTrackList(state.tracks)
    }

    companion object {
        private const val SAVED_INPUT = "SAVED_INPUT"
        private const val INPUT_DEF = ""
    }
}