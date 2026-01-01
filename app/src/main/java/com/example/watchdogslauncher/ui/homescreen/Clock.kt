
package com.example.watchdogslauncher.ui.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

@Composable
fun Clock(format: String = "24h", modifier: Modifier = Modifier) {
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var glitchTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = System.currentTimeMillis()
            if (Random.nextFloat() > 0.9f) {
                glitchTrigger++
            }
        }
    }

    val timePattern = if (format == "12h") "hh:mm a" else "HH:mm"
    val datePattern = "EEE, MMM dd"
    
    val sdf = SimpleDateFormat(timePattern, Locale.getDefault())
    val dateSdf = SimpleDateFormat(datePattern, Locale.getDefault())
    val timeText = sdf.format(Date(currentTime))
    val dateText = dateSdf.format(Date(currentTime))

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlitchText(
            text = timeText,
            glitchTrigger = glitchTrigger,
            fontSize = 32.sp
        )
        Text(
            text = dateText,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            ),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun GlitchText(text: String, glitchTrigger: Int, fontSize: androidx.compose.ui.unit.TextUnit = 64.sp) {
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
            color = MaterialTheme.colorScheme.primary,
            fontSize = fontSize,
            fontFamily = FontFamily.Monospace
        ),
        modifier = Modifier.graphicsLayer {
            translationX = glitchOffset
        }
    )
}
