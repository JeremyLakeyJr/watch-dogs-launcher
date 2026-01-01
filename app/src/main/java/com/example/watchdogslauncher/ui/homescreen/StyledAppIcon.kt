
package com.example.watchdogslauncher.ui.homescreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchdogslauncher.model.AppInfo
import com.example.watchdogslauncher.ui.theme.WdBlue

@Composable
fun StyledAppIcon(
    app: AppInfo,
    iconSize: Float = 1.0f,
    showLabel: Boolean = true,
    onClick: () -> Unit = {}
) {
    var visible by remember { mutableStateOf(false) }
    val scanProgress by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 500, delayMillis = 100)
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size((64 * iconSize).dp)
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
        
        if (showLabel) {
            Text(
                text = app.label.toString(),
                style = MaterialTheme.typography.bodySmall,
                fontSize = (12 * iconSize).sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .width((64 * iconSize).dp)
            )
        }
    }
}
