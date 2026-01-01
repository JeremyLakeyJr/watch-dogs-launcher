package com.example.watchdogslauncher.ui.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.watchdogslauncher.model.AppInfo
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppDrawer(
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    onAppDragStart: (app: AppInfo) -> Unit,
    onAppDragEnd: () -> Unit = {},
    onAppDrag: (dragAmount: Offset) -> Unit = {},
    onClose: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            // Header with close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Apps (${apps.size})",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onClose) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 96.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                items(apps) { app ->
                    Box(
                        modifier = Modifier
                            .pointerInput(app) {
                                detectDragGestures(
                                    onDragStart = {
                                        onAppDragStart(app)
                                    },
                                    onDragEnd = { onAppDragEnd() },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        onAppDrag(dragAmount)
                                    }
                                )
                            }
                    ) {
                        AppIcon(
                            app = app,
                            modifier = Modifier.clickable { onAppClick(app) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppIcon(
    app: AppInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberDrawablePainter(drawable = app.icon),
            contentDescription = app.label.toString(),
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = app.label.toString(),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

