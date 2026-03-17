package com.example.playlistmaker.mediateka.playlists.domain.impl

import android.net.Uri
import com.example.playlistmaker.mediateka.playlists.domain.api.StorageInteractor
import com.example.playlistmaker.mediateka.playlists.domain.api.StorageRepository

class StorageInteractorImpl(private val repository: StorageRepository) : StorageInteractor {
    override suspend fun saveImageToPrivateStorage(uri: Uri): Uri {
        return repository.saveImageToPrivateStorage(uri)
    }
}