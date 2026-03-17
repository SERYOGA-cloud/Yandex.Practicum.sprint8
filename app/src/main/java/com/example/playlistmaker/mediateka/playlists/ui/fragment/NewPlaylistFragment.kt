package com.example.playlistmaker.mediateka.playlists.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.mediateka.playlists.ui.viewModel.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    private var binding: FragmentNewPlaylistBinding? = null
    private val viewModel: NewPlaylistViewModel by viewModel()

    private lateinit var confirmExitDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeToastMessage().observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        viewModel.observeShouldCloseScreen().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        viewModel.observeIsCreateButtonEnabled().observe(viewLifecycleOwner) { isEnabled ->
            showCreateButtonEnabled(isEnabled)
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    requireContext().contentResolver.takePersistableUriPermission(uri, flag)

                    binding!!.newPhotoView.setImageURI(uri)
                    viewModel.onImageLoaded(uri)
                } else {
                    Log.d("NEW_PLAYLIST_FRAGMENT", "No media selected")
                }
            }

        confirmExitDialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomDialog)
                .setTitle(R.string.confirm_closing_creating_list)
                .setMessage(R.string.all_unsaved_data_will_be_lost)
                .setPositiveButton(R.string.terminate) { _, _ ->
                    findNavController().navigateUp()
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }

        binding!!.apply {
            newPlaylistToolbar.setNavigationOnClickListener {
                setupBackNavigation()
            }

            newPhotoView.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            newPlaylistTitleEdittext.doOnTextChanged { text, _, _, _ ->
                viewModel.onTitleTextChanged(text.toString())
            }

            newPlaylistDescriptionEdittext.doOnTextChanged { text, _, _, _ ->
                viewModel.onDescriptionChanged(text.toString())
            }

            createButton.setOnClickListener {
                viewModel.onCreatePlaylist()
            }
        }

    }

    private fun showCreateButtonEnabled(enabled: Boolean) {
        binding?.createButton?.isEnabled = enabled
    }

    private fun setupBackNavigation() {
        if (viewModel.checkShowDialogConditions()) {
            confirmExitDialog.show()
        } else {
            findNavController().navigateUp()
        }
    }
}