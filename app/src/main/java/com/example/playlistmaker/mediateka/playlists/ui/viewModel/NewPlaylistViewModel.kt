package com.example.playlistmaker.mediateka.playlists.ui.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.mediateka.playlists.domain.api.PlaylistsInteractor
import com.example.playlistmaker.mediateka.playlists.domain.api.StorageInteractor
import com.example.playlistmaker.mediateka.playlists.domain.entity.Playlist
import com.example.playlistmaker.sharing.domain.api.StringResourceProvider
import com.example.playlistmaker.util.SingleLiveEvent
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val storageInteractor: StorageInteractor,
    private val stringResProvider: StringResourceProvider
) : ViewModel() {

    private var playlistTitle: String = ""
    private var playlistCoverUri: Uri? = null
    private var playlistDescription: String? = null

    private val _isCreateButtonEnabled = MutableLiveData<Boolean>(false)
    fun observeIsCreateButtonEnabled(): LiveData<Boolean> = _isCreateButtonEnabled

    private val _toastMessage = SingleLiveEvent<String>()
    fun observeToastMessage(): LiveData<String> = _toastMessage

    private val _shouldCloseScreen = SingleLiveEvent<Unit>()
    fun observeShouldCloseScreen(): LiveData<Unit> = _shouldCloseScreen

    fun onTitleTextChanged(newTitle: String) {
        playlistTitle = newTitle
        _isCreateButtonEnabled.value = playlistTitle.isNotEmpty()
    }

    fun onDescriptionChanged(newDescription: String) {
        playlistDescription = newDescription
    }

    fun onImageLoaded(imageUri: Uri) {
        playlistCoverUri = imageUri
    }

    fun onCreatePlaylist() {
        viewModelScope.launch {
            var savedImageUri: Uri? = null
            if (playlistCoverUri != null) {
                savedImageUri = storageInteractor.saveImageToPrivateStorage(playlistCoverUri!!)
            }
            if (playlistTitle.isNotEmpty()) {
                playlistsInteractor.createPlaylist(
                    Playlist(
                        id = 0,
                        tracks = null,
                        title = playlistTitle,
                        description = playlistDescription,
                        coverUri = savedImageUri
                    )
                )
                _toastMessage.value = stringResProvider.getPlaylistCreatedMsg(playlistTitle)
                _shouldCloseScreen.postValue(Unit)
            }
        }
    }

    fun checkShowDialogConditions(): Boolean {
        val hasSomeText: Boolean = !playlistTitle.isEmpty() || !playlistDescription.isNullOrEmpty()
        val hasImageLoaded: Boolean = playlistCoverUri != null

        return hasSomeText || hasImageLoaded
    }
}