package com.example.watchdogslauncher.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private fun createDarkColorScheme(primary: Color) = darkColorScheme(
    primary = primary,
    secondary = HackerPurple,
    tertiary = GlitchPink,
    background = DarkBlue,
    surface = DarkBlue,
    onPrimary = OffWhite,
    onSecondary = OffWhite,
    onTertiary = OffWhite,
    onBackground = OffWhite,
    onSurface = OffWhite,
)

@Composable
fun WatchDogsLauncherTheme(
    themeColorName: String = "HackerBlue",
    content: @Composable () -> Unit
) {
    val primaryColor = when (themeColorName) {
        "HackerPurple" -> HackerPurple
        "GlitchPink" -> GlitchPink
        else -> HackerBlue
    }
    val colorScheme = createDarkColorScheme(primary = primaryColor)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
