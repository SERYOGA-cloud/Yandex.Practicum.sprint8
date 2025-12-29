package com.example.playlistmaker.search.ui.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.search.domain.consumer.TrackConsumer
import com.example.playlistmaker.search.domain.entity.Resource
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.search.ui.entity.SearchState

class SearchViewModel(
    private val trackSearchInteractor: TrackSearchInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private var searchQuery: String = ""
    private var isEditTextInFocus: Boolean = false

    private var searchStateLiveData = MutableLiveData<SearchState>(SearchState.Empty)
    fun observeSearchState(): LiveData<SearchState> = searchStateLiveData

    init {
        updateState()
    }

    private val trackSearchConsumer = TrackConsumer { result ->
        handler.post {
            when (result) {
                is Resource.Error -> {
                    overrideStateLiveData(SearchState.PlaceHolder.NetworkError())
                }

                is Resource.Success -> {
                    if (result.results.isEmpty()) {
                        overrideStateLiveData(SearchState.PlaceHolder.NothingFound())
                    } else {
                        overrideStateLiveData(SearchState.SearchResults(result.results))
                    }
                }
            }
        }
    }

    private val searchRunnable = Runnable { searchRequest(searchQuery) }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }

    fun onEditTextFocusChange(hasFocus: Boolean) {
        isEditTextInFocus = hasFocus
        updateState()
    }

    fun onQueryChanged(newQuery: String) {
        searchQuery = newQuery
        updateState()
    }

    fun onItemClicked(track: Track) {
        searchHistoryInteractor.saveTrackToHistory(track)
    }

    fun onClearHistoryButtonClicked() {
        searchHistoryInteractor.clearHistory()
        overrideStateLiveData(SearchState.Empty)
    }

    fun onTryAgainButtonClicked() {
        startSearch(false)
    }

    private fun searchRequest(searchText: String) {
        if (searchText.isEmpty()) {
            updateState()
            return
        }
        overrideStateLiveData(SearchState.Loading)
        trackSearchInteractor.searchTracks(searchText, trackSearchConsumer)
    }

    private fun startSearch(isDebounce: Boolean) {
        if (isDebounce) {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        } else {
            handler.post(searchRunnable)
        }
    }

    private fun overrideStateLiveData(state: SearchState) {
        searchStateLiveData.value = state
    }

    private fun updateState() {
        handler.removeCallbacks(searchRunnable)

        if (searchQuery.isNotEmpty()) {
            startSearch(true)
            return
        }

        val history = searchHistoryInteractor.getHistoryList()
        if (history.isNotEmpty()) {
            overrideStateLiveData(SearchState.History(history))
        } else {
            overrideStateLiveData(SearchState.Empty)
        }
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
