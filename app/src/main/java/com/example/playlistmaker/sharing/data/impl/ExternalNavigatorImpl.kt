package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.playlist.data.dto.PlaylistDto
import com.example.playlistmaker.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.entity.EmailData
import com.example.playlistmaker.util.Util
import com.example.playlistmaker.util.Util.Companion.millisToMmSs

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink(link: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, link)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun openLink(link: String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun openEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse(emailData.scheme)
        intent.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf(emailData.emailAddress)
        )
        intent.putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
        intent.putExtra(Intent.EXTRA_TEXT, emailData.text)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun sharePlaylist(playlist: PlaylistDto) {
        val playlistMessage = getPlaylistMessage(playlist)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, playlistMessage)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val chooserIntent = Intent.createChooser(intent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooserIntent)
    }

    private fun getPlaylistMessage(playlist: PlaylistDto): String {
        val stringBuilder = StringBuilder()

        // Название
        stringBuilder.append(context.getString(R.string.playlist, playlist.title))

        // Описание, если есть
        playlist.description?.let { description ->
            stringBuilder
                .append("\n")
                .append(context.getString(R.string.description))
                .append(": ")
                .append(description)
        }

        // Количество треков
        val tracksCount = playlist.tracks?.size ?: 0
        val ending = Util.getRusNumeralTrackEnding(tracksCount)
        stringBuilder
            .append("\n")
            .append(context.getString(R.string.tracks, tracksCount, ending))

        // Нумерованный список треков
        playlist.tracks?.forEachIndexed { index, track ->
            stringBuilder
                .append("\n")
                .append(
                    "${index + 1}. ${track.artistName} - ${track.trackName} (${
                        millisToMmSs(track.trackTimeMillis)
                    })"
                )
        }

        return stringBuilder.toString()
    }
}
