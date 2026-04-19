package com.sensecolor.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Teal40,
    onPrimary = Color.White,
    secondary = Sienna40,
    onSecondary = Color.White,
    tertiary = Amber40,
    onTertiary = Color.White,
    background = CreamBackground,
    onBackground = Color(0xFF1C1208),
    surface = CreamSurface,
    onSurface = Color(0xFF1C1208),
    surfaceVariant = CreamSurfaceVariant,
    onSurfaceVariant = Color(0xFF4A3F35),
    error = ErrorRed,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Teal80,
    onPrimary = Color(0xFF003731),
    secondary = Sienna80,
    onSecondary = Color(0xFF2E1810),
    tertiary = Amber80,
    onTertiary = Color(0xFF3E2800),
    background = DarkBackground,
    onBackground = Color(0xFFF0E9DF),
    surface = DarkSurface,
    onSurface = Color(0xFFF0E9DF),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFD4C4B0),
    error = Color(0xFFCF6679),
    onError = Color(0xFF640021)
)

@Composable
fun SenseColorTheme(
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
