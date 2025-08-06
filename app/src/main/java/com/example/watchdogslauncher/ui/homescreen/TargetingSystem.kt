
package com.example.watchdogslauncher.ui.homescreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import com.example.watchdogslauncher.model.AppInfo
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TargetingSystem(apps: List<AppInfo>) {
    var rotationAngle by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    rotationAngle += dragAmount.x / 5f
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = this.center
            val radius = size.minDimension / 3

            // Draw the targeting reticle
            drawCircle(Color.White, radius = 10f, center = center)
            drawLine(Color.White, start = Offset(center.x - 20, center.y), end = Offset(center.x + 20, center.y))
            drawLine(Color.White, start = Offset(center.x, center.y - 20), end = Offset(center.x, center.y + 20))

            // Draw the app icons in a circle
            apps.forEachIndexed { index, app ->
                val angle = (index.toFloat() / apps.size) * 2 * Math.PI + rotationAngle
                val x = center.x + radius * cos(angle).toFloat()
                val y = center.y + radius * sin(angle).toFloat()

                drawIntoCanvas { canvas ->
                    val drawable = app.icon
                    drawable.setBounds(x.toInt() - 32, y.toInt() - 32, x.toInt() + 32, y.toInt() + 32)
                    drawable.draw(canvas.nativeCanvas)
                }
            }
        }
    }
}
