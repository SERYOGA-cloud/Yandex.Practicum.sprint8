package com.example.playlistmaker.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var binding: FragmentSettingsBinding? = null
    val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.apply {
            viewModel.observeIsDarkThemeEnabled().observe(viewLifecycleOwner) {
                themeSwitcher.isChecked = it
            }

            themeSwitcher.setOnCheckedChangeListener { _, checked ->
                viewModel.onThemeSwitcherClicked(checked)
            }

            shareButton.setOnClickListener {
                viewModel.shareApp()
            }

            supportButton.setOnClickListener {
                viewModel.openSupport()
            }

            userAgreementButton.setOnClickListener {
                viewModel.openTerms()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}