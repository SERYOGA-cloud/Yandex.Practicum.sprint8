package com.example.playlistmaker.search.ui.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.consumer.TrackConsumer
import com.example.playlistmaker.search.domain.entity.Resource
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.search.ui.entity.SearchState

class SearchViewModel : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private var searchQuery: String = ""
    private var isEditTextInFocus: Boolean = false

    private val trackSearchInteractor = Creator.provideTrackSearchInteractor()
    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor()

    private var searchStateLiveData = MutableLiveData<SearchState>(SearchState.Empty)
    fun observeSearchState(): LiveData<SearchState> = searchStateLiveData

    private val trackSearchConsumer = TrackConsumer { result ->
        handler.post {
            when (result) {
                is Resource.Error -> {
                    postState(SearchState.PlaceHolder.NetworkError())
                }

                is Resource.Success -> {
                    if (result.results.isEmpty()) {
                        postState(SearchState.PlaceHolder.NothingFound())
                    } else {
                        postState(SearchState.SearchResults(result.results))
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
        postState(SearchState.Empty)
    }

    fun onTryAgainButtonClicked() {
        startSearch(false)
    }

    private fun searchRequest(searchText: String) {
        if (searchText.isEmpty()) {
            updateState()
            return
        }
        postState(SearchState.Loading)
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

    private fun postState(state: SearchState) {
        searchStateLiveData.value = state
    }

    private fun updateState() {
        when {
            isEditTextInFocus && searchQuery.isNotEmpty() -> {
                startSearch(true)        // postState(Loading) -> *search* -> postState()
            }

            isEditTextInFocus && searchQuery.isEmpty() -> {
                postState(SearchState.History(searchHistoryInteractor.getHistoryList()))
            }

            else -> postState(SearchState.Empty)
        }
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
