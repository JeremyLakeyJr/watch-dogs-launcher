package com.example.watchdogslauncher.ui.homescreen

import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.watchdogslauncher.model.AppInfo
import kotlin.math.roundToInt

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val apps = remember {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        packageManager.queryIntentActivities(intent, 0).mapNotNull { resolveInfo ->
            try {
                val appInfo = packageManager.getApplicationInfo(resolveInfo.activityInfo.packageName, 0)
                AppInfo(
                    label = resolveInfo.loadLabel(packageManager),
                    packageName = resolveInfo.activityInfo.packageName,
                    icon = resolveInfo.loadIcon(packageManager),
                    installTime = packageManager.getPackageInfo(resolveInfo.activityInfo.packageName, 0).firstInstallTime
                )
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }
    }

    var showAppDrawer by remember { mutableStateOf(false) }
    val desktopApps = remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    var draggedApp by remember { mutableStateOf<AppInfo?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { showAppDrawer = true })
                }
        ) {
            AnimatedBackground()
            DataStreamOverlay()

            // Desktop shortcuts
            LazyVerticalGrid(columns = GridCells.Fixed(4), modifier = Modifier.padding(16.dp)) {
                items(desktopApps.value) { app ->
                    AppIcon(app = app)
                }
            }

            if (showAppDrawer) {
                AppDrawer(
                    apps = apps,
                    onAppClick = { app ->
                        val intent = packageManager.getLaunchIntentForPackage(app.packageName.toString())
                        intent?.let { context.startActivity(it) }
                    },
                    onAppDragStart = { app ->
                        draggedApp = app
                        dragOffset = Offset.Zero
                    },
                    onAppDragEnd = {
                        // Check if the app was dropped on the desktop
                        if (dragOffset.y < -100) { // Simple check for now
                            draggedApp?.let { app ->
                                if (!desktopApps.value.contains(app)) {
                                    desktopApps.value = desktopApps.value + app
                                }
                            }
                        }
                        draggedApp = null
                        showAppDrawer = false
                    },
                    onAppDrag = { amount ->
                        dragOffset += amount
                    }
                )
            }

            // Draw the dragged app
            draggedApp?.let {
                AppIcon(
                    app = it,
                    modifier = Modifier.offset { IntOffset(dragOffset.x.roundToInt(), dragOffset.y.roundToInt()) }
                )
            }
        }
    }
}
