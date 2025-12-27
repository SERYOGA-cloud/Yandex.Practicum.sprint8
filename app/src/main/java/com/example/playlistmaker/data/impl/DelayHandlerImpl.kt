package com.example.playlistmaker.data.impl

import android.os.Handler
import com.example.playlistmaker.domain.api.Delay

class DelayHandlerImpl(private val handler: Handler) : Delay {

    private var runnable: Runnable? = null

    override fun postDelayed(interval: Long, callback: () -> Unit) {

        runnable = Runnable { callback.invoke() }
        handler.postDelayed(runnable!!, interval)
    }

    override fun stop() {
        runnable?.let { handler.removeCallbacks(it) }
        runnable = null
    }
}