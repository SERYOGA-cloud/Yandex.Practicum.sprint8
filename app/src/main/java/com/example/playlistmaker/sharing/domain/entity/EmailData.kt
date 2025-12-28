package com.example.playlistmaker.sharing.domain.entity

data class EmailData(
    val emailAddress: List<String>,
    val subject: String,
    val text: String
) {
    val scheme: String = "mailto:"
}