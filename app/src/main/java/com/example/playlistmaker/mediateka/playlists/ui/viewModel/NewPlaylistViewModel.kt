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
    val playlistsInteractor: PlaylistsInteractor,
    val storageInteractor: StorageInteractor,
    val stringResProvider: StringResourceProvider,
    val existingPlaylist: Playlist?
) : ViewModel() {

    private var playlistTitle: String = ""
    private var playlistCoverUri: Uri? = null
    private var playlistDescription: String? = null

    init {
        if (existingPlaylist != null) {
            playlistTitle = existingPlaylist.title
            playlistCoverUri = existingPlaylist.coverUri
            playlistDescription = existingPlaylist.description
        }
    }

    private val _isCreateButtonEnabled = MutableLiveData<Boolean>(existingPlaylist != null)
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

    fun onUpdatePlaylist() {
        if (checkNothingChanged()) {
            _toastMessage.value = stringResProvider.getNoChangesInPlaylistMadeMsg()
            return
        } else {
            viewModelScope.launch {

                val updatedPlaylist = existingPlaylist!!.copy(
                    title = playlistTitle,
                    description = playlistDescription,
                    coverUri = playlistCoverUri
                )
                playlistsInteractor.updatePlaylist(updatedPlaylist)
                _shouldCloseScreen.postValue(Unit)
            }
        }

    }

    fun checkShowDialogConditions(): Boolean {
        if (existingPlaylist == null) {
            val hasSomeText: Boolean =
                !playlistTitle.isEmpty() || !playlistDescription.isNullOrEmpty()
            val hasImageLoaded: Boolean = playlistCoverUri != null

            return hasSomeText || hasImageLoaded
        } else {
            return false
        }
    }

    private fun checkNothingChanged(): Boolean {
        return playlistTitle == existingPlaylist?.title
                && playlistDescription == existingPlaylist.description
                && playlistCoverUri == existingPlaylist.coverUri
    }
}