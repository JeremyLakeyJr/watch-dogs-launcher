
package com.example.watchdogslauncher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun WatchDogsLauncherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    theme: ThemeModel = DefaultTheme,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = theme.primaryColor,
            secondary = theme.secondaryColor,
            background = theme.backgroundColor,
            surface = theme.backgroundColor,
            onPrimary = theme.textColor,
            onSecondary = theme.textColor,
            onBackground = theme.textColor,
            onSurface = theme.textColor,
        )
    } else {
        lightColorScheme(
            primary = theme.primaryColor,
            secondary = theme.secondaryColor,
            background = theme.backgroundColor,
            surface = theme.backgroundColor,
            onPrimary = theme.textColor,
            onSecondary = theme.textColor,
            onBackground = theme.textColor,
            onSurface = theme.textColor,
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
