
package com.example.watchdogslauncher.ui.homescreen

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.example.watchdogslauncher.ui.theme.WdGreen
import kotlinx.coroutines.isActive
import kotlin.random.Random

@Composable
fun DataStreamOverlay() {
    val time by produceState(0L) {
        while (isActive) {
            withInfiniteAnimationFrameMillis {
                value = it
            }
        }
    }

    val textPaint = remember {
        android.graphics.Paint().apply {
            isAntiAlias = true
            textSize = 24f
            color = WdGreen.copy(alpha = 0.2f).toArgb()
        }
    }

    val columnData = remember {
        val characters = "0123456789ABCDEF"
        (0..20).map {
            val text = (0..50).map { characters.random() }.joinToString("")
            val speed = Random.nextFloat() * 2f + 1f
            val x = Random.nextFloat() * 1000
            Triple(text, speed, x)
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawIntoCanvas { canvas ->
            columnData.forEach { (text, speed, x) ->
                val y = (time * 0.1f * speed) % (size.height * 1.5f)
                canvas.nativeCanvas.drawText(text, x, y, textPaint)
            }
        }
    }
}
