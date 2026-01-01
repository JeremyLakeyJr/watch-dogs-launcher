package com.example.watchdogslauncher.ui.homescreen

import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.watchdogslauncher.data.SettingsRepository
import com.example.watchdogslauncher.model.AppInfo
import com.example.watchdogslauncher.model.LauncherSettings
import com.example.watchdogslauncher.ui.SettingsScreen
import com.example.watchdogslauncher.ui.bitchat.BitChatScreen
import com.example.watchdogslauncher.ui.terminal.EnhancedTerminal
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun EnhancedHomeScreen() {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val settingsRepository = remember { SettingsRepository(context) }
    val settings by settingsRepository.settings.collectAsState(initial = LauncherSettings())
    
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
        }.sortedBy { it.label.toString() }
    }

    var showAppDrawer by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var showTerminal by remember { mutableStateOf(false) }
    var showBitChat by remember { mutableStateOf(false) }
    val desktopApps = remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    
    // Gesture detection state
    var dragStart by remember { mutableStateOf(Offset.Zero) }
    var lastTapTime by remember { mutableStateOf(0L) }

    fun handleGestureAction(action: String) {
        when (action) {
            "AppDrawer" -> showAppDrawer = true
            "Terminal" -> showTerminal = true
            "Settings" -> showSettings = true
            "Notifications" -> { /* TODO: Implement notifications */ }
            "QuickSettings" -> { /* TODO: Implement quick settings */ }
            "BitChat" -> showBitChat = true
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset -> dragStart = offset },
                        onDragEnd = {
                            val currentDrag = Offset.Zero
                            // Detect swipe direction
                            val dx = currentDrag.x - dragStart.x
                            val dy = currentDrag.y - dragStart.y
                            
                            if (abs(dx) > abs(dy)) {
                                // Horizontal swipe
                                if (abs(dx) > 100) {
                                    if (dx > 0) {
                                        handleGestureAction(settings.gestureSwipeRight)
                                    } else {
                                        handleGestureAction(settings.gestureSwipeLeft)
                                    }
                                }
                            } else {
                                // Vertical swipe
                                if (abs(dy) > 100) {
                                    if (dy > 0) {
                                        handleGestureAction(settings.gestureSwipeDown)
                                    } else {
                                        handleGestureAction(settings.gestureSwipeUp)
                                    }
                                }
                            }
                        }
                    ) { _, dragAmount ->
                        // Track drag amount if needed
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastTapTime < 300) {
                                // Double tap detected
                                handleGestureAction(settings.gestureDoubleTap)
                            }
                            lastTapTime = currentTime
                        },
                        onLongPress = {
                            handleGestureAction(settings.gestureLongPress)
                        }
                    )
                }
        ) {
            // Dynamic Background
            DynamicBackground(backgroundType = settings.backgroundEffect)
            
            // Data stream overlay
            if (settings.enableParticleEffects) {
                DataStreamOverlay()
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top widgets area
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (settings.enableClock) {
                        Clock(format = settings.clockFormat)
                    }
                    if (settings.enableSystemStats) {
                        SystemStatsWidget()
                    }
                }
                
                // Desktop apps grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(settings.gridColumns),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(desktopApps.value) { app ->
                        StyledAppIcon(
                            app = app,
                            iconSize = settings.iconSize,
                            showLabel = settings.showLabels,
                            onClick = {
                                val intent = packageManager.getLaunchIntentForPackage(app.packageName.toString())
                                intent?.let { context.startActivity(it) }
                            }
                        )
                    }
                }
                
                // Bottom hint text
                if (desktopApps.value.isEmpty()) {
                    Text(
                        text = "Swipe up to access apps\nDouble tap for terminal",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            // App Drawer
            AnimatedVisibility(
                visible = showAppDrawer,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                AppDrawer(
                    apps = apps,
                    onAppClick = { app ->
                        val intent = packageManager.getLaunchIntentForPackage(app.packageName.toString())
                        intent?.let { context.startActivity(it) }
                        showAppDrawer = false
                    },
                    onAppDragStart = { app ->
                        // TODO: Implement drag to desktop
                    },
                    onClose = { showAppDrawer = false }
                )
            }

            // Settings
            AnimatedVisibility(
                visible = showSettings,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    SettingsScreen(settingsRepository = settingsRepository)
                }
            }

            // Terminal
            AnimatedVisibility(
                visible = showTerminal,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                EnhancedTerminal(
                    apps = apps,
                    onDismiss = { showTerminal = false },
                    enableAutocomplete = settings.terminalAutocomplete,
                    historySize = settings.terminalHistorySize
                )
            }

            // BitChat
            AnimatedVisibility(
                visible = showBitChat,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                BitChatScreen()
            }
        }
    }
}
