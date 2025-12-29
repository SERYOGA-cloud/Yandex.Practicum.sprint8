package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.entity.PlayerState

// Вместо обычного интерактора на случай, если ссылка на трек == null (чтобы активити не крашилась)
class FailedAudioPlayerInteractorImpl() : AudioPlayerInteractor {
    override fun onPlayButtonClicked(): PlayerState {
        throw Exception("Ошибка. Отсутствует ссылка на отрывок трека")
    }

    override fun pausePlayer() {}

    override fun getCurrentPositionConverted(): String {
        return "00:00"
    }

    override fun releasePlayer() {}
}