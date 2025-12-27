package com.example.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.WindowCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
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
                    Intent(this, PlayerActivity::class.java).apply {
                        putExtra("track_name", currentTrack.trackName)
                        putExtra("artist_name", currentTrack.artistName)
                        putExtra("track_time_converted", currentTrack.trackTimeConverted)
                        putExtra("collection_name", currentTrack.collectionName)
                        putExtra("release_date", currentTrack.releaseDate)
                        putExtra("primary_genre_name", currentTrack.primaryGenreName)
                        putExtra("country", currentTrack.country)
                        putExtra("artwork_url_100", currentTrack.artworkUrl100)
                        putExtra("preview_url", currentTrack.previewUrl)
                    }
                startActivity(playerIntent)
            }
        )
        binding.recyclerViewLayout.recyclerView.adapter = adapter

        binding.searchToolbar.setNavigationOnClickListener {
            finish()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchEditTextClearButton.isVisible = !s.isNullOrEmpty()
                viewModel.onQueryChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.editTextSearch.addTextChangedListener(textWatcher)
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
//            trackSearchInteractor.searchTracks(searchSavedInput, trackSearchConsumer)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private fun renderState(state: SearchState) {

        // Установка видимостей элементов
        binding.apply {
            progressBar.isGone = !state.isShowLoading
            recyclerViewLayout.root.isGone = !state.isShowHistory && !state.isShowSearchResults
            recyclerViewLayout.youSearchedText.isGone = !state.isShowHistory
            recyclerViewLayout.historyClearButton.isGone = !state.isShowHistory
            placeholderLayout.placeholderImage.isGone = !state.isShowPlaceHolder
            placeholderLayout.placeholderText.isGone = !state.isShowPlaceHolder
            placeholderLayout.tryAgainButton.isGone = !state.showTryAgainButton
        }

        // Обновление элементов
        when (state) {
            is SearchState.Empty -> {}
            is SearchState.History -> adapter.updateTrackList(state.trackHistory)
            is SearchState.Loading -> {}
            is SearchState.PlaceHolder -> preparePlaceholder(state)
            is SearchState.SearchResults -> adapter.updateTrackList(state.tracks)
        }
    }

    private fun preparePlaceholder(state: SearchState.PlaceHolder) {
        binding.placeholderLayout.apply {
            when (state) {
                is SearchState.PlaceHolder.NetworkError -> {

                    placeholderImage.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@SearchActivity,
                            R.drawable.img_connection_issues
                        )
                    )
                    placeholderText.setText(R.string.connection_issues_check_connection)
                }

                is SearchState.PlaceHolder.NothingFound -> {
                    placeholderImage.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@SearchActivity,
                            R.drawable.img_nothing_found
                        )
                    )
                    placeholderText.setText(R.string.nothing_found)
                }
            }
        }
    }

    companion object {
        private const val SAVED_INPUT = "SAVED_INPUT"
        private const val INPUT_DEF = ""
    }
}