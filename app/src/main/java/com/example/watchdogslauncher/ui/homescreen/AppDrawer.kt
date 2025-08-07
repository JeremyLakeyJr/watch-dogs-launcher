package com.example.watchdogslauncher.ui.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.watchdogslauncher.model.AppInfo
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppDrawer(
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    onAppDragStart: (app: AppInfo) -> Unit,
    onAppDragEnd: () -> Unit,
    onAppDrag: (dragAmount: Offset) -> Unit
) {
    Surface(
        color = Color.Black.copy(alpha = 0.9f),
        modifier = Modifier.fillMaxSize()
    ) {
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
            contentDescription = app.label.toString()
        )
        Text(text = app.label.toString(), color = Color.White)
    }
}
