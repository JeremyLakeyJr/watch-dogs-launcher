
package com.example.watchdogslauncher.ui.homescreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.watchdogslauncher.model.AppInfo
import com.example.watchdogslauncher.ui.theme.WdBlue

@Composable
fun StyledAppIcon(app: AppInfo) {
    var visible by remember { mutableStateOf(false) }
    val scanProgress by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 500, delayMillis = 100)
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .border(
                width = 2.dp,
                color = WdBlue,
                shape = RoundedCornerShape(8.dp)
            )
            .graphicsLayer {
                alpha = scanProgress
            }
            .clip(RoundedCornerShape(8.dp))
    ) {
        AppIcon(app = app)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = -size.height + (size.height * scanProgress)
                }
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            WdBlue.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}
