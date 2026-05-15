package com.example.playlistmaker.mediateka.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.app.ui.theme.PlaylistMakerTheme
import com.example.playlistmaker.mediateka.ui.composable.MediatekaScreen
import com.example.playlistmaker.root.ui.activity.RootActivity

class MediatekaComposeFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bottomNavHeightDp = (requireActivity() as RootActivity).getBottomNavHeight().dp

        return ComposeView(requireContext()).apply {
            setContent {
                PlaylistMakerTheme {

                    MediatekaScreen(findNavController(), bottomNavHeightDp)
                }
            }
        }
    }
}