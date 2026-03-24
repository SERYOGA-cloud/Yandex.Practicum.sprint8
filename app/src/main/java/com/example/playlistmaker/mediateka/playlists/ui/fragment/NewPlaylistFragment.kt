package com.example.playlistmaker.mediateka.playlists.ui.fragment

import android.content.Intent
import android.graphics.Outline
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.mediateka.playlists.ui.viewModel.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NewPlaylistViewModel
    private var confirmExitDialog: MaterialAlertDialogBuilder? = null

    // 1. Регистрация перенесена в свойства класса
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            requireContext().contentResolver.takePersistableUriPermission(uri, flag)

            loadCover(uri.toString())
            viewModel.onImageLoaded(uri)
        } else {
            Log.d("NEW_PLAYLIST_FRAGMENT", "No media selected")
        }
    }

    private val existingPlaylist: Playlist? by lazy {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARGS_PLAYLIST, Playlist::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARGS_PLAYLIST)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = getKoin().get { parametersOf(existingPlaylist) }
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillPlaylistInfo()
        setupListeners()
        setupObservers()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    setupBackNavigation()
                }
            }
        )
    }

    private fun setupListeners() {
        binding.apply {
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
                if (existingPlaylist == null) {
                    viewModel.onCreatePlaylist()
                } else {
                    viewModel.onUpdatePlaylist()
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.observeToastMessage().observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        viewModel.observeShouldCloseScreen().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        viewModel.observeIsCreateButtonEnabled().observe(viewLifecycleOwner) { isEnabled ->
            binding.createButton.isEnabled = isEnabled
        }
    }

    private fun fillPlaylistInfo() {
        existingPlaylist?.let { playlist ->
            binding.apply {
                playlist.coverUri?.let { loadCover(it.toString()) }
                newPlaylistTitleEdittext.setText(playlist.title)
                newPlaylistDescriptionEdittext.setText(playlist.description)
                createButton.setText(R.string.save)
            }
        }
    }

    // Универсальный метод загрузки изображения
    private fun loadCover(uri: String) {
        val radius = resources.getDimensionPixelSize(R.dimen.new_playlist_photo_corner_radius)
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .transform(RoundedCorners(radius))
            .into(binding.newPhotoView)
    }

    private fun setupBackNavigation() {
        if (viewModel.checkShowDialogConditions()) {
            showConfirmDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showConfirmDialog() {
        if (confirmExitDialog == null) {
            confirmExitDialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomDialog)
                .setTitle(R.string.confirm_closing_creating_list)
                .setMessage(R.string.all_unsaved_data_will_be_lost)
                .setPositiveButton(R.string.terminate) { _, _ ->
                    findNavController().navigateUp()
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
        }
        confirmExitDialog?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        confirmExitDialog = null // Очищаем ссылку на диалог
    }

    companion object {
        const val ARGS_PLAYLIST = "playlist"
    }
}