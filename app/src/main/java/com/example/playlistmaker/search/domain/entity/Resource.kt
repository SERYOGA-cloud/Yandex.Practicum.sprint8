package com.example.playlistmaker.search.domain.entity

sealed class Resource<T> {
    data class Success<T>(val results: T) : Resource<T>()
    data class Error<T>(val errorCode: Int) : Resource<T>()
}
