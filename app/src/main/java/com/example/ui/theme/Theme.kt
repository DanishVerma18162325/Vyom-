package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VyomLightColorScheme = lightColorScheme(
    primary = VyomAtmosphereBlue,
    onPrimary = Color.White,
    primaryContainer = VyomTagBackground,
    onPrimaryContainer = VyomTagText,
    secondary = VyomAtmospherePurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3E8FF),
    onSecondaryContainer = Color(0xFF581C87),
    tertiary = VyomAtmosphereCyan,
    background = VyomPureWhite,
    onBackground = VyomInkPrimary,
    surface = VyomPureWhite,
    onSurface = VyomInkPrimary,
    surfaceVariant = VyomSurfaceSubtle,
    onSurfaceVariant = VyomInkSecondary,
    outline = VyomGlassBorder,
    outlineVariant = VyomDivider,
    error = VyomError,
    onError = Color.White
)

private val VyomDarkColorScheme = darkColorScheme(
    primary = Color(0xFF60A5FA),
    onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF1E293B),
    onPrimaryContainer = Color(0xFF93C5FD),
    secondary = Color(0xFFA78BFA),
    onSecondary = Color(0xFF1E1B4B),
    background = Color(0xFF090D16),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF0F172A),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF334155),
    outlineVariant = Color(0xFF1E293B)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Pure White Dynamic is the primary signature identity of VYOM
    val colorScheme = if (darkTheme) VyomDarkColorScheme else VyomLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
