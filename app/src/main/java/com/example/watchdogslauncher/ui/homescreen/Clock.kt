
package com.example.watchdogslauncher.ui.homescreen

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.watchdogslauncher.ui.theme.WdBlue
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

@Composable
fun Clock() {
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var glitchTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = System.currentTimeMillis()
            if (Random.nextFloat() > 0.8f) {
                glitchTrigger++
            }
        }
    }

    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val timeText = sdf.format(Date(currentTime))

    GlitchText(
        text = timeText,
        glitchTrigger = glitchTrigger
    )
}

@Composable
fun GlitchText(text: String, glitchTrigger: Int) {
    var localGlitchTrigger by remember { mutableStateOf(glitchTrigger) }
    var glitchOffset by remember { mutableStateOf(0f) }
    var glitchColor by remember { mutableStateOf(Color.Transparent) }

    LaunchedEffect(glitchTrigger) {
        if (glitchTrigger != localGlitchTrigger) {
            localGlitchTrigger = glitchTrigger
            glitchOffset = (Random.nextFloat() - 0.5f) * 10f
            glitchColor = listOf(Color.Red, Color.Green, Color.Blue).random().copy(alpha = 0.5f)
            delay(Random.nextLong(50, 150))
            glitchOffset = 0f
            glitchColor = Color.Transparent
        }
    }

    Text(
        text = text,
        style = TextStyle(
            color = WdBlue,
            fontSize = 64.sp
        ),
        modifier = Modifier.graphicsLayer {
            translationX = glitchOffset
        }
    )
}
