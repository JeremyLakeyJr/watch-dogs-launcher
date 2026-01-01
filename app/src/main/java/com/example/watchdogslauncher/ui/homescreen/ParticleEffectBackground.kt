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
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var size: Float,
    val alpha: Float
)

@Composable
fun ParticleEffectBackground() {
    val time by produceState(0L) {
        while (isActive) {
            withInfiniteAnimationFrameMillis {
                value = it
            }
        }
    }

    val particles = produceState(initialValue = emptyList<Particle>()) {
        val random = Random(System.currentTimeMillis())
        value = List(50) {
            Particle(
                x = random.nextFloat(),
                y = random.nextFloat(),
                vx = (random.nextFloat() - 0.5f) * 0.0001f,
                vy = (random.nextFloat() - 0.5f) * 0.0001f,
                size = random.nextFloat() * 3f + 1f,
                alpha = random.nextFloat() * 0.5f + 0.2f
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val deltaTime = time * 0.01f

        particles.value.forEach { particle ->
            // Update particle position
            particle.x += particle.vx * deltaTime
            particle.y += particle.vy * deltaTime

            // Wrap around screen
            if (particle.x < 0) particle.x += 1f
            if (particle.x > 1) particle.x -= 1f
            if (particle.y < 0) particle.y += 1f
            if (particle.y > 1) particle.y -= 1f

            // Draw particle
            drawCircle(
                color = WdBlue.copy(alpha = particle.alpha),
                radius = particle.size,
                center = Offset(particle.x * width, particle.y * height)
            )
        }

        // Draw connections between nearby particles
        particles.value.forEachIndexed { i, p1 ->
            particles.value.drop(i + 1).forEach { p2 ->
                val dx = (p1.x - p2.x) * width
                val dy = (p1.y - p2.y) * height
                val distance = kotlin.math.sqrt(dx * dx + dy * dy)

                if (distance < 150f) {
                    val alpha = (1f - distance / 150f) * 0.2f
                    drawLine(
                        color = WdBlue.copy(alpha = alpha),
                        start = Offset(p1.x * width, p1.y * height),
                        end = Offset(p2.x * width, p2.y * height),
                        strokeWidth = 1f
                    )
                }
            }
        }
    }
}
