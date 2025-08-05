
package com.example.watchdogslauncher.ui.homescreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.watchdogslauncher.ui.theme.WdBlue
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun HexagonalGrid() {
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val linePulse by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val random = remember { Random(System.currentTimeMillis()) }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val hexSize = 60f
        val hexWidth = sqrt(3f) * hexSize
        val hexHeight = 2 * hexSize

        for (row in 0..(height / (hexHeight * 0.75f)).toInt()) {
            for (col in 0..(width / hexWidth).toInt()) {
                val x = col * hexWidth + (if (row % 2 == 0) 0f else hexWidth / 2)
                val y = row * hexHeight * 0.75f

                val hexPath = Path().apply {
                    for (i in 0..5) {
                        val angle = 60 * i - 30
                        val rad = Math.toRadians(angle.toDouble()).toFloat()
                        val px = x + hexSize * cos(rad)
                        val py = y + hexSize * sin(rad)
                        if (i == 0) moveTo(px, py) else lineTo(px, py)
                    }
                    close()
                }
                drawPath(
                    path = hexPath,
                    color = WdBlue.copy(alpha = 0.1f * pulse),
                    style = Stroke(width = 1f)
                )

                // Add random lines and dots
                if (random.nextFloat() < 0.1f) {
                    val startX = x + random.nextFloat() * hexWidth - hexWidth / 2
                    val startY = y + random.nextFloat() * hexHeight - hexHeight / 2
                    val endX = x + random.nextFloat() * hexWidth - hexWidth / 2
                    val endY = y + random.nextFloat() * hexHeight - hexHeight / 2
                    drawLine(
                        color = WdBlue.copy(alpha = 0.2f * linePulse),
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 1f
                    )
                }

                if (random.nextFloat() < 0.2f) {
                    val dotX = x + random.nextFloat() * hexWidth - hexWidth / 2
                    val dotY = y + random.nextFloat() * hexHeight - hexHeight / 2
                    drawCircle(
                        color = WdBlue.copy(alpha = 0.5f * pulse),
                        radius = random.nextFloat() * 2f,
                        center = Offset(dotX, dotY)
                    )
                }
            }
        }
    }
}
