package com.lukesleeman.currencyconverter.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = DarkBackground,
    surface = DarkSurface,
    primaryContainer = DarkPrimaryContainer,
    secondaryContainer = DarkAccent,
    surfaceVariant = DarkSurface,
    onBackground = DarkOnSurface,
    onSurface = DarkOnSurface,
    onPrimaryContainer = DarkOnSurface,
    onSecondaryContainer = DarkOnSurface,
    onSurfaceVariant = DarkOnSurface
)

private val LightColorScheme = lightColorScheme(
    background = LightBackground,
    surface = LightSurface,
    primaryContainer = LightPrimaryContainer,
    secondaryContainer = LightAccent,
    surfaceVariant = LightSurface,
    onBackground = LightOnSurface,
    onSurface = LightOnSurface,
    onPrimaryContainer = LightOnSurface,
    onSecondaryContainer = Color.White,
    onSurfaceVariant = LightOnSurface
)

@Composable
fun CurrencyConverterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}