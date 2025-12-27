package com.example.playlistmaker.domain.entity

sealed class Resource<T> {
    data class Success<T>(val results: T) : Resource<T>()
    data class Error<T>(val resultCode: Int) : Resource<T>()
}
