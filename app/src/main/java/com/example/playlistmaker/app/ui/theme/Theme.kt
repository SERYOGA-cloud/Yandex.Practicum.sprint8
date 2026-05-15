package com.example.playlistmaker.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
private fun lightColorScheme() = lightColorScheme(
    background = white,
    primary = white,
    onPrimary = ypBlack,
    onPrimaryFixed = grey,
    onPrimaryContainer = lightGrey,
    onPrimaryFixedVariant = grey,
    secondary = blue,
    onSecondary = white,
    tertiary = white,
    onTertiary = ypBlack
)

@Composable
private fun darkColorScheme() = darkColorScheme(
    background = ypBlack,
    primary = ypBlack,
    onPrimary = white,
    onPrimaryFixed = white,
    onPrimaryContainer = white,
    onPrimaryFixedVariant = ypBlack,
    secondary = ypBlack,
    onSecondary = white,
    tertiary = white,
    onTertiary = ypBlack
)

val LocalTypography = staticCompositionLocalOf<CustomTypography> {
    error("No CustomTypography provided")
}

@Composable
fun PlaylistMakerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        com.example.playlistmaker.app.ui.theme.darkColorScheme()
    } else {
        com.example.playlistmaker.app.ui.theme.lightColorScheme()
    }

    CompositionLocalProvider(
        LocalTypography provides Typography
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}