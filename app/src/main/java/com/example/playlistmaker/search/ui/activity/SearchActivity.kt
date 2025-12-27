package com.example.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.WindowCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.consumer.TrackConsumer
import com.example.playlistmaker.search.domain.entity.Resource
import com.example.playlistmaker.search.domain.entity.Track

class SearchActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    private val trackSearchInteractor = Creator.provideTrackSearchInteractor()
    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor()

    private var searchSavedInput: String = INPUT_DEF
    private val searchResultsList = mutableListOf<Track>()

    @SuppressLint("NotifyDataSetChanged")
    private val trackSearchConsumer = TrackConsumer { result ->
        handler.post {
            hideProgressBar()

            when (result) {
                is Resource.Error -> {
                    hideSearchResults()
                    showSearchPlaceholder(CONNECTION_ISSUES)
                }

                is Resource.Success -> {
                    searchResultsList.clear()
                    searchResultsList.addAll(result.results)
                    searchResultsAdapter.notifyDataSetChanged()
                    showSearchResults()

                    if (searchResultsList.isEmpty()) {
                        hideSearchResults()
                        showSearchPlaceholder(NOTHING_FOUND)
                    }
                }
            }
        }
    }

    private val searchRunnable = Runnable {
        showProgressBar()
        trackSearchInteractor.searchTracks(searchSavedInput, trackSearchConsumer)
    }

    private lateinit var searchResultsAdapter: TrackAdapter
    private lateinit var searchHistoryAdapter: TrackAdapter

    private lateinit var binding: ActivitySearchBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.searchResultsRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.historyLayout.searchHistoryRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        searchHistoryAdapter =
            TrackAdapter(searchHistoryInteractor.getHistoryList(), searchHistoryInteractor)
        binding.historyLayout.searchHistoryRecyclerView.adapter = searchHistoryAdapter

        searchResultsAdapter = TrackAdapter(searchResultsList, searchHistoryInteractor)
        binding.searchResultsRecyclerView.adapter = searchResultsAdapter

        binding.searchToolbar.setNavigationOnClickListener {
            finish()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchEditTextClearButton.isVisible = !s.isNullOrEmpty()
                if (binding.editTextSearch.hasFocus()) {

                    if (s?.isEmpty() == true) {
                        handler.removeCallbacks(searchRunnable)
                        searchResultsList.clear()
                        searchResultsAdapter.notifyDataSetChanged()
                        hideSearchResults()
                        showSearchHistory()

                    } else {
                        hideSearchHistory()
                        searchSavedInput = s.toString()
                        searchDebounce()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.editTextSearch.addTextChangedListener(textWatcher)
        binding.editTextSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.editTextSearch.text.isEmpty()) {
                hideSearchResults()
                showSearchHistory()
            } else hideSearchHistory()
        }

        binding.searchEditTextClearButton.setOnClickListener {
            handler.removeCallbacks(searchRunnable)
            binding.editTextSearch.text.clear()
            searchResultsList.clear()
            searchResultsAdapter.notifyDataSetChanged()
            hideSearchResults()


            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            if (currentFocus != null) {
                inputMethodManager?.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
            binding.editTextSearch.clearFocus()
        }

        binding.historyLayout.searchHistoryClearButton.setOnClickListener {
            searchHistoryInteractor.clearHistory()
            searchHistoryAdapter.notifyDataSetChanged()
            hideSearchHistory()
        }

        binding.placeholderLayout.searchPlaceholderButton.setOnClickListener {
            hideSearchPlaceholders()
            trackSearchInteractor.searchTracks(searchSavedInput, trackSearchConsumer)
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
            trackSearchInteractor.searchTracks(searchSavedInput, trackSearchConsumer)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        searchHistoryAdapter.notifyDataSetChanged()
    }

    private fun searchDebounce() {
        if (searchSavedInput.isEmpty()) {
            return
        } else {
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchPlaceholder(reason: Int) {
        hideSearchHistory()
        binding.placeholderLayout.apply {
            when (reason) {
                NOTHING_FOUND -> {
                    searchResultsList.clear()
                    searchResultsAdapter.notifyDataSetChanged()

                    searchPlaceholderImage.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@SearchActivity,
                            R.drawable.img_nothing_found
                        )
                    )
                    searchPlaceholderText.setText(R.string.nothing_found)
                    searchPlaceholderImage.isVisible = true
                    searchPlaceholderText.isVisible = true
                }

                CONNECTION_ISSUES -> {
                    val message =
                        getString(R.string.connection_issues) + "\n\n" + getString(R.string.load_failed_check_connection)
                    searchResultsList.clear()
                    searchResultsAdapter.notifyDataSetChanged()

                    searchPlaceholderImage.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@SearchActivity,
                            R.drawable.img_connection_issues
                        )
                    )
                    searchPlaceholderText.text = message
                    searchPlaceholderImage.isVisible = true
                    searchPlaceholderText.isVisible = true
                    searchPlaceholderButton.isVisible = true
                }
            }
        }
    }

    private fun showSearchHistory() {
        val history = searchHistoryInteractor.getHistoryList()
        binding.historyLayout.apply {
            if (history.isNotEmpty()) {
                hideSearchPlaceholders()
                root.isVisible = true
                searchHistoryClearButton.isVisible = true
            }
        }
    }

    private fun hideSearchHistory() {
        binding.historyLayout.apply {
            root.isGone = true
            searchHistoryClearButton.isGone = true
        }
    }

    private fun showSearchResults() {
        binding.searchResultsRecyclerView.isVisible = true
    }

    private fun hideSearchResults() {
        binding.searchResultsRecyclerView.isGone = true
    }

    private fun hideSearchPlaceholders() {
        binding.placeholderLayout.apply {
            searchPlaceholderImage.isGone = true
            searchPlaceholderText.isGone = true
            searchPlaceholderButton.isGone = true
        }
    }

    private fun showProgressBar() {
        binding.progressCircular.isVisible = true
    }

    private fun hideProgressBar() {
        binding.progressCircular.isGone = true
    }

    companion object {
        private const val SAVED_INPUT = "SAVED_INPUT"
        private const val INPUT_DEF = ""
        private const val NOTHING_FOUND: Int = 0
        private const val CONNECTION_ISSUES: Int = -1
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}