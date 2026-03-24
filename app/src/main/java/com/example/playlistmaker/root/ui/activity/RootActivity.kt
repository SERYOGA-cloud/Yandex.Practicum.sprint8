package com.example.playlistmaker.root.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.playerFragment, R.id.newPlaylistFragment, R.id.playlistFragment -> {
                    showBottomNav(binding, false)
                }

                else -> {
                    showBottomNav(binding, true)
                }
            }

        }

        binding.bottomNavView.setupWithNavController(navController)
    }

    private fun showBottomNav(b: ActivityRootBinding, show: Boolean) {
        if (!show) {
            b.bottomNavView.isGone = true
            b.navMenuTopLine.isGone = true
        } else {
            b.bottomNavView.isVisible = true
            b.navMenuTopLine.isVisible = true
        }
    }
}