package com.example.playlistmaker.domain.api

interface Delay {

    fun postDelayed(interval: Long, callback: () -> Unit)

    fun stop()
}