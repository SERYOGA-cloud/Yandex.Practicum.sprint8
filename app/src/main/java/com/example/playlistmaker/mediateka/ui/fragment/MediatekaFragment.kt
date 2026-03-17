package com.example.playlistmaker.mediateka.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediatekaBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator

class MediatekaFragment : Fragment() {

    private lateinit var tabMediator: TabLayoutMediator

    private var binding: FragmentMediatekaBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediatekaBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.apply {
            // Добаляем нижний отступ для ViewPager,
            // равный высоте bottom navigation для правильной отрисовки списков
            viewPager.post {
                view.post {
                    val bottomNav =
                        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)
                    bottomNav?.let { bottomNavigationView ->
                        val params =
                            binding?.viewPager?.layoutParams as ViewGroup.MarginLayoutParams
                        params.bottomMargin = bottomNavigationView.height
                        binding?.viewPager?.layoutParams = params
                    }
                }
            }

            viewPager.adapter = MediatekaViewPagerAdapter(
                fragmentManager = childFragmentManager,
                lifecycle = lifecycle
            )

            tabMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.favorite_tracks)
                    1 -> tab.text = getString(R.string.playlists)
                }
            }
            tabMediator.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
        binding = null
    }
}