package com.example.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.presentation.designsystem.MovieColors

private val DarkColorScheme = darkColorScheme(
    primary = MovieColors.MovieAccent,
    onPrimary = MovieColors.TextPrimary,
    secondary = MovieColors.MovieAccentSoft,
    background = MovieColors.PrimaryBackground,
    onBackground = MovieColors.TextPrimary,
    surface = MovieColors.Surface,
    onSurface = MovieColors.TextPrimary,
    surfaceVariant = MovieColors.SurfaceVariant,
    onSurfaceVariant = MovieColors.TextSecondary,
    error = MovieColors.ErrorColor,
    outline = MovieColors.DividerColor
)

private val LightColorScheme = lightColorScheme(
    primary = NetflixRed,
    background = LightBackground,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    onBackground = OnSurfaceLight
)

@Composable
fun MovieTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
