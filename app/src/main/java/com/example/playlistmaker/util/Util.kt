package com.example.playlistmaker.util

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale

class Util {
    companion object {
        fun millisToMmSs(millis: Int): String {
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
        }

        fun mmSsToMillis(mmSs: String): Int {
            val parts = mmSs.split(":")

            val minutes = parts[0].toInt()
            val seconds = parts[1].toInt()

            return (minutes * 60 + seconds) * 1000
        }

        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            ).toInt()
        }

        fun getCoverArtwork512(url100: String?): String? {
            return url100?.replaceAfterLast('/', "512x512bb.jpg")
        }
    }
}