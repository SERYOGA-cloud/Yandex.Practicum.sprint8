package com.example.playlistmaker.mediateka.playlists.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.mediateka.playlists.domain.api.StorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StorageRepositoryImpl(private val context: Context) : StorageRepository {
    override suspend fun saveImageToPrivateStorage(uri: Uri): Uri {
        return withContext(Dispatchers.IO) {

            val filePath = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "playlistCovers"
            )
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val file = File(filePath, "IMG_$timeStamp.jpg")
            val inPutStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            BitmapFactory
                .decodeStream(inPutStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            Uri.fromFile(file)
        }
    }
}