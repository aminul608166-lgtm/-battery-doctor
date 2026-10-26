package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldPrimaryDarkTheme,
    onPrimary = Color(0xFF062E14),
    primaryContainer = EmeraldContainerDark,
    onPrimaryContainer = Color(0xFFB8F5CD),
    secondary = Color(0xFF66BB6A),
    onSecondary = Color(0xFF082912),
    secondaryContainer = EmeraldCardDark,
    onSecondaryContainer = Color(0xFFD7F0D8),
    tertiary = Color(0xFF38BDF8),
    background = EmeraldSurfaceDark,
    onBackground = Color(0xFFEAF7ED),
    surface = Color(0xFF102317),
    onSurface = Color(0xFFEAF7ED),
    surfaceVariant = EmeraldCardDark,
    onSurfaceVariant = Color(0xFFB4D6BE),
    error = Color(0xFFEF5350)
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = EmeraldLight,
    onPrimaryContainer = EmeraldDark,
    secondary = Color(0xFF2E7D32),
    onSecondary = Color.White,
    secondaryContainer = EmeraldCard,
    onSecondaryContainer = Color(0xFF0D3B1E),
    tertiary = EmeraldShopCard,
    onTertiary = EmeraldDark,
    background = MintBackground,
    onBackground = Color(0xFF122117),
    surface = Color.White,
    onSurface = Color(0xFF122117),
    surfaceVariant = EmeraldLight,
    onSurfaceVariant = Color(0xFF3E5946),
    error = AlertRed
)

@Composable
fun MyApplicationTheme(
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
