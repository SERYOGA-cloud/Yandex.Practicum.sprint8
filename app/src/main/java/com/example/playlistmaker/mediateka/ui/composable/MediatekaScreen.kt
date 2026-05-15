package com.example.playlistmaker.mediateka.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.playlistmaker.R
import com.example.playlistmaker.app.ui.theme.LocalTypography
import com.example.playlistmaker.mediateka.favorites.ui.composable.FavoritesPage
import com.example.playlistmaker.mediateka.playlists.ui.composable.PlaylistsPage
import com.example.playlistmaker.playlist.ui.fragment.PlaylistFragment
import com.example.playlistmaker.search.domain.entity.Track
import com.example.playlistmaker.search.ui.composable.CustomTopAppBar
import com.example.playlistmaker.util.ARGS_TRACK
import kotlinx.coroutines.launch

@Composable
fun MediatekaScreen(navController: NavController, bottomNavHeight: Dp) {
    val pagerState = rememberPagerState(
        pageCount = { 2 }
    )
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CustomTopAppBar(stringResource(R.string.mediateka))
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { contentPadding ->

        Column(
            Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.background,
                indicator = { tabPositions ->
                    val indicatorModifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)

                    TabRowDefaults.Indicator(
                        modifier = indicatorModifier,
                        height = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                divider = {}
            ) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    text = {
                        Text(
                            stringResource(R.string.favorite_tracks),
                            style = LocalTypography.current.mediatekaTabText,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                )

                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    text = {
                        Text(
                            stringResource(R.string.playlists),
                            style = LocalTypography.current.mediatekaTabText,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 -> {
                        FavoritesPage(
                            bottomNavHeight = bottomNavHeight,
                            onTrackClicked = { track: Track ->
                                navController.navigate(
                                    R.id.action_global_to_playerFragment,
                                    bundleOf(ARGS_TRACK to track)
                                )
                            }
                        )
                    }

                    1 -> {
                        PlaylistsPage(
                            bottomNavHeight = bottomNavHeight,
                            onButtonClicked = { navController.navigate(R.id.action_global_to_newPlaylistFragment) },
                            onItemClicked = {
                                navController.navigate(
                                    R.id.action_global_to_playlistFragment, bundleOf(
                                        PlaylistFragment.ARGS_PLAYLIST to it
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}