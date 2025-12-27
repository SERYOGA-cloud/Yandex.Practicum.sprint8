package com.example.playlistmaker.presentation.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.WindowCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.consumer.TrackConsumer
import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import android.view.View
import android.view.WindowManager

class SearchActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    private val trackSearchInteractor = Creator.provideTrackSearchInteractor()
    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor()

    private var searchSavedInput: String = INPUT_DEF
    private val searchResultsList = mutableListOf<Track>()
    private val historyList = mutableListOf<Track>()

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

    private lateinit var placeHolderImg: ImageView
    private lateinit var placeHolderText: TextView
    private lateinit var placeHolderTryAgainButton: Button
    private lateinit var clearHistoryButton: Button
    private lateinit var searchHistoryView: LinearLayout
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private lateinit var searchInput: EditText
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerLayout: View
    private lateinit var placeholderLayout: View

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // views
        searchInput = findViewById(R.id.edit_text_search)
        val clearEditTextButton: ImageView = findViewById(R.id.search_edit_text_clear_button)
        progressBar = findViewById(R.id.progress_bar)

        recyclerLayout = findViewById(R.id.recycler_view_layout)
        placeholderLayout = findViewById(R.id.placeholder_layout)

        searchResultsRecyclerView = recyclerLayout.findViewById(R.id.recycler_view)

        placeHolderImg = findViewById(R.id.search_placeholder_image)
        placeHolderText = findViewById(R.id.search_placeholder_text)
        placeHolderTryAgainButton = findViewById(R.id.search_placeholder_button)

        searchHistoryView = findViewById(R.id.search_history_view_group)
        searchHistoryRecyclerView = findViewById(R.id.search_history_recycler_view)
        clearHistoryButton = findViewById(R.id.search_history_clear_button)

        // списки
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this)

        searchHistoryAdapter = TrackAdapter(historyList, searchHistoryInteractor)
        searchHistoryRecyclerView.adapter = searchHistoryAdapter

        searchResultsAdapter = TrackAdapter(searchResultsList, searchHistoryInteractor)
        searchResultsRecyclerView.adapter = searchResultsAdapter

        // убрать клавиатуру и фокус
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        findViewById<View>(android.R.id.content).requestFocus()

        // показать историю ПОСЛЕ первого layout-pass
        searchHistoryView.post {
            showSearchHistory()
        }

        // watcher
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearEditTextButton.isVisible = !s.isNullOrEmpty()

                if (s.isNullOrEmpty()) {
                    handler.removeCallbacks(searchRunnable)
                    searchResultsList.clear()
                    searchResultsAdapter.notifyDataSetChanged()
                    hideSearchResults()
                    hideSearchPlaceholders()
                    showSearchHistory()
                } else {
                    hideSearchHistory()
                    hideSearchPlaceholders()
                    searchSavedInput = s.toString()
                    searchDebounce()
                }
            }
        })

        // очистка
        clearEditTextButton.setOnClickListener {
            handler.removeCallbacks(searchRunnable)
            searchInput.text.clear()

            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchInput.windowToken, 0)

            findViewById<View>(android.R.id.content).requestFocus()
            showSearchHistory()
        }

        clearHistoryButton.setOnClickListener {
            searchHistoryInteractor.clearHistory()
            historyList.clear()
            searchHistoryAdapter.notifyDataSetChanged()
            hideSearchHistory()
        }

        placeHolderTryAgainButton.setOnClickListener {
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
            searchInput.setText(searchSavedInput)
            trackSearchInteractor.searchTracks(searchSavedInput, trackSearchConsumer)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        searchInput.clearFocus()
        findViewById<View>(android.R.id.content).requestFocus()

        if (searchInput.text.isNullOrEmpty()) {
            showSearchHistory()
        }
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
        hideSearchResults()

        recyclerLayout.isGone = true
        placeholderLayout.isVisible = true

        when (reason) {
            NOTHING_FOUND -> {
                searchResultsList.clear()
                searchResultsAdapter.notifyDataSetChanged()

                placeHolderImg.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.img_nothing_found
                    )
                )
                placeHolderText.setText(R.string.nothing_found)
                placeHolderImg.isVisible = true
                placeHolderText.isVisible = true
                placeHolderTryAgainButton.isGone = true
            }

            CONNECTION_ISSUES -> {
                val message =
                    getString(R.string.connection_issues) + "\n\n" +
                            getString(R.string.load_failed_check_connection)

                searchResultsList.clear()
                searchResultsAdapter.notifyDataSetChanged()

                placeHolderImg.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.img_connection_issues
                    )
                )
                placeHolderText.text = message
                placeHolderImg.isVisible = true
                placeHolderText.isVisible = true
                placeHolderTryAgainButton.isVisible = true
            }
        }
    }

    private fun showSearchHistory() {
        val history = searchHistoryInteractor.getHistoryList()

        historyList.clear()
        historyList.addAll(history)
        searchHistoryAdapter.notifyDataSetChanged()

        placeholderLayout.isGone = true
        recyclerLayout.isGone = true
        searchHistoryView.isVisible = historyList.isNotEmpty()

        if (historyList.isNotEmpty()) {
            searchHistoryView.isVisible = true
            clearHistoryButton.isVisible = true
        } else {
            hideSearchHistory()
        }
    }

    private fun hideSearchHistory() {
        searchHistoryView.isGone = true
        clearHistoryButton.isGone = true
    }

    private fun showSearchResults() {
        placeholderLayout.isGone = true
        searchHistoryView.isGone = true
        recyclerLayout.isVisible = true
    }

    private fun hideSearchResults() {
        recyclerLayout.isGone = true
    }

    private fun hideSearchPlaceholders() {
        placeholderLayout.isGone = true
    }

    private fun showProgressBar() {
        progressBar.isVisible = true
    }

    private fun hideProgressBar() {
        progressBar.isGone = true
    }

    companion object {
        private const val SAVED_INPUT = "SAVED_INPUT"
        private const val INPUT_DEF = ""
        private const val NOTHING_FOUND: Int = 0
        private const val CONNECTION_ISSUES: Int = -1
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}