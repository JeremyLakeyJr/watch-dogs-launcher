
package com.example.watchdogslauncher.ui.homescreen

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.watchdogslauncher.ui.theme.WdBlue
import kotlinx.coroutines.isActive

@Composable
fun AnimatedBackground() {
    val time by produceState(0L) {
        while (isActive) {
            withInfiniteAnimationFrameMillis {
                value = it
            }
        }
    }

    val lineSpeed = 0.1f
    val lineDensity = 50f

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val offset = (time * lineSpeed) % lineDensity

        for (i in 0..(width / lineDensity).toInt()) {
            val x = i * lineDensity + offset
            drawLine(
                color = WdBlue.copy(alpha = 0.1f),
                start = Offset(x, 0f),
                end = Offset(x, height),
                strokeWidth = 1f
            )
        }

        for (i in 0..(height / lineDensity).toInt()) {
            val y = i * lineDensity + offset
            drawLine(
                color = WdBlue.copy(alpha = 0.1f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
        }
    }
}
