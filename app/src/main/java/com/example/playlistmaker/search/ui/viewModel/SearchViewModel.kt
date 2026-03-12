package com.example.playlistmaker.search.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackSearchInteractor
import com.example.playlistmaker.search.domain.entity.Resource
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.search.ui.entity.SearchState
import com.example.playlistmaker.util.SEARCH_DEBOUNCE_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackSearchInteractor: TrackSearchInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private var searchJob: Job? = null

    private var searchQuery: String = ""
    private var isEditTextInFocus: Boolean = false
    private var isForceShowResults: Boolean = false

    private val savedSearchResults = mutableListOf<Track>()

    private var searchStateLiveData = MutableLiveData<SearchState>(SearchState.Empty)
    fun observeSearchState(): LiveData<SearchState> = searchStateLiveData

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

        viewModelScope.launch {
            trackSearchInteractor
                .searchTracks(searchText)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            savedSearchResults.clear()
                            overrideStateLiveData(SearchState.PlaceHolder.NetworkError())
                        }

                        is Resource.Success -> {
                            if (result.results.isEmpty()) {
                                savedSearchResults.clear()
                                overrideStateLiveData(SearchState.PlaceHolder.NothingFound())
                            } else {
                                savedSearchResults.clear()
                                savedSearchResults.addAll(result.results)
                                overrideStateLiveData(SearchState.Content(result.results))
                            }
                        }
                    }
                }
        }
    }

    private fun startSearch(isDebounce: Boolean) {
        searchJob?.cancel()
        if (isDebounce) {
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                searchRequest(searchQuery)
            }
        } else {
            searchRequest(searchQuery)
        }
    }

    private fun overrideStateLiveData(state: SearchState) {
        searchStateLiveData.value = state
    }

    private fun updateState() {

        if (!isForceShowResults) {
            when {
                isEditTextInFocus && searchQuery.isNotEmpty() -> {
                    startSearch(true)        // postState(Loading) -> *search* -> postState()
                }

                isEditTextInFocus && searchQuery.isEmpty() -> {
                    viewModelScope.launch {
                        searchHistoryInteractor.getHistoryList().collect { searchHistoryList ->
                            if (searchHistoryList.isNotEmpty()) {
                                overrideStateLiveData(SearchState.History(searchHistoryList))
                            } else {
                                overrideStateLiveData(SearchState.Empty)
                            }
                        }
                    }
                }

                else -> overrideStateLiveData(SearchState.Empty)
            }

        } else {
            overrideStateLiveData(SearchState.Content(savedSearchResults))
        }
    }

    fun onReturnFromPlayer() {
        isForceShowResults = savedSearchResults.isNotEmpty()
    }
}