package com.example.watchdogslauncher.ui.homescreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DynamicBackground(
    backgroundType: String,
    modifier: Modifier = Modifier
) {
    when (backgroundType) {
        "Grid" -> AnimatedBackground()
        "Hexagonal" -> HexagonalGrid()
        "Matrix" -> MatrixBackground()
        "Particles" -> ParticleEffectBackground()
        "None" -> { /* No background */ }
        else -> AnimatedBackground()
    }
}
