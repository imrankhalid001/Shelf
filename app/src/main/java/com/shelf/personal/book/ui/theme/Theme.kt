package com.shelf.personal.book.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AmberAccentDark,
    onPrimary = DarkInk,
    primaryContainer = DeepAmberSurface,
    background = DarkInk,
    surface = DarkSurface,
    surfaceVariant = ElevatedCharcoal,
    onBackground = PaperHigh,
    onSurface = PaperHigh,
    onSurfaceVariant = MutedWarm,
    outline = BorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = AmberAccent,
    onPrimary = PaperLight,
    primaryContainer = AmberSurface,
    background = PaperLight,
    surface = Color.White,
    surfaceVariant = PaperHigh,
    onBackground = DeepInk,
    onSurface = DeepInk,
    onSurfaceVariant = WarmGrey,
    outline = WarmBorder
)

@Composable
fun ShelfTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
