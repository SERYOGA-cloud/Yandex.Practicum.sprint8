package com.example.playlistmaker.player.domain.entity

import com.example.playlistmaker.util.TRACK_PROGRESS_TIME_DEFAULT

sealed class PlayerState(open val progress: String) {

    data class Playing(override val progress: String) : PlayerState(progress)

    data class Paused(override val progress: String) : PlayerState(progress)

    data object Prepared : PlayerState(TRACK_PROGRESS_TIME_DEFAULT)

    data object Default : PlayerState(TRACK_PROGRESS_TIME_DEFAULT)
}