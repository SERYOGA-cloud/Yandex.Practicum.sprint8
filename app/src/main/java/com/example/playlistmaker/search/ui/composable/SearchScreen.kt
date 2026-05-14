package com.example.playlistmaker.search.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.search.ui.entity.SearchState.Content
import com.example.playlistmaker.search.ui.entity.SearchState.Empty
import com.example.playlistmaker.search.ui.entity.SearchState.History
import com.example.playlistmaker.search.ui.entity.SearchState.Loading
import com.example.playlistmaker.search.ui.entity.SearchState.PlaceHolder.NetworkError
import com.example.playlistmaker.search.ui.entity.SearchState.PlaceHolder.NothingFound
import com.example.playlistmaker.search.ui.viewModel.SearchViewModel
import com.example.playlistmaker.util.ARGS_TRACK
import com.example.playlistmaker.util.RETURNING_FROM_PLAYER

@Composable
fun SearchScreen(
    viewModel: SearchViewModel?,
    navController: NavController?,
) {
    val state = viewModel?.observeSearchState()?.observeAsState()?.value
    val onItemClick = { track: Track ->
        viewModel?.onItemClicked(track)

        navController?.currentBackStackEntry?.savedStateHandle
            ?.set(RETURNING_FROM_PLAYER, true)

        navController?.navigate(
            R.id.action_searchComposeFragment_to_playerFragment,
            bundleOf(ARGS_TRACK to track)
        )
    }
    val query = viewModel?.searchQuery?.collectAsState()?.value ?: ""

    // Для принудительного показа результатов поиска после возврата с другого экрана
    navController?.currentBackStackEntry?.savedStateHandle
        ?.getLiveData<Boolean>(RETURNING_FROM_PLAYER)
        ?.observe(LocalLifecycleOwner.current) { returning ->
            if (returning) {
                viewModel?.onReturnFromPlayer()

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    RETURNING_FROM_PLAYER,
                    false
                )
            }
        }

    Scaffold(
        topBar = {
            CustomTopAppBar(stringResource(R.string.search))
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { contentPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues = contentPadding)
        ) {

            CustomSearchBar(
                initialQuery = query,
                onQueryChanged = { newQuery ->
                    viewModel?.onQueryChanged(newQuery)
                },
                onFocusChanged = { isFocused ->
                    viewModel?.onEditTextFocusChange(isFocused)
                }
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.search_bar_bottom_spacer)))

            when (state) {
                is Content -> {
                    TrackList(items = state.tracks, onItemCLicked = { track -> onItemClick(track) })
                }

                is Empty -> {}
                is History -> {
                    TrackList(
                        items = state.trackHistory,
                        onItemCLicked = { track -> onItemClick(track) },
                        isHistory = true,
                        onClearHistoryClicked = { viewModel.onClearHistoryButtonClicked() }
                    )
                }

                is Loading -> {
                    CustomProgressIndicator()
                }

                is NetworkError -> Placeholder(state.textId, state.imageId) {
                    viewModel.onTryAgainButtonClicked()
                }

                is NothingFound -> Placeholder(state.textId, state.imageId) {}

                null -> {}
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun SearchScreenLightPreview() {
    PlaylistMakerTheme {
        SearchScreen(
            viewModel = null,
            navController = null
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SearchScreenNightPreview() {
    PlaylistMakerTheme {
        SearchScreen(
            viewModel = null,
            navController = null
        )
    }
}