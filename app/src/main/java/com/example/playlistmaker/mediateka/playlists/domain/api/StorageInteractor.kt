package com.example.playlistmaker.mediateka.playlists.domain.api

import android.net.Uri

interface StorageInteractor {

    suspend fun saveImageToPrivateStorage(uri: Uri): Uri
}