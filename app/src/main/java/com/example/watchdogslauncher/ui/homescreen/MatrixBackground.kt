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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.example.watchdogslauncher.ui.theme.MatrixGreen
import kotlinx.coroutines.isActive
import kotlin.random.Random

data class MatrixColumn(
    var y: Float,
    val x: Float,
    val speed: Float,
    val length: Int,
    val chars: List<String>
)

@Composable
fun MatrixBackground() {
    val time by produceState(0L) {
        while (isActive) {
            withInfiniteAnimationFrameMillis {
                value = it
            }
        }
    }

    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(fontSize = 14.sp, color = MatrixGreen)
    
    val columns = produceState(initialValue = emptyList<MatrixColumn>()) {
        val random = Random(System.currentTimeMillis())
        val chars = "ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toList().map { it.toString() }
        
        value = List(30) {
            MatrixColumn(
                y = random.nextFloat() * -500f,
                x = it * 40f,
                speed = random.nextFloat() * 3f + 1f,
                length = random.nextInt(10, 30),
                chars = List(30) { chars.random() }
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val height = size.height
        val deltaTime = time * 0.01f

        columns.value.forEach { column ->
            // Update column position
            column.y += column.speed * deltaTime
            
            // Reset when column goes off screen
            if (column.y > height + column.length * 20f) {
                column.y = -column.length * 20f
            }

            // Draw each character in the column
            for (i in 0 until column.length) {
                val charY = column.y + i * 20f
                if (charY >= 0 && charY <= height) {
                    val alpha = when {
                        i == 0 -> 1f
                        i < 5 -> 0.7f
                        i < 10 -> 0.5f
                        else -> 0.3f
                    }
                    
                    drawText(
                        textMeasurer = textMeasurer,
                        text = column.chars[i],
                        topLeft = Offset(column.x, charY),
                        style = textStyle.copy(color = MatrixGreen.copy(alpha = alpha))
                    )
                }
            }
        }
    }
}
