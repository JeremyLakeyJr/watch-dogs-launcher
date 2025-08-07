package com.example.watchdogslauncher.ui.homescreen

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.watchdogslauncher.data.SettingsRepository
import com.example.watchdogslauncher.model.AppInfo
import com.example.watchdogslauncher.ui.SettingsScreen
import com.example.watchdogslauncher.ui.bitchat.BitChatScreen

@Composable
fun SystemStatsWidget() {
    val context = LocalContext.current
    var batteryLevel by remember { mutableStateOf(0) }
    var ramUsage by remember { mutableStateOf(0L) }
    var appDrawerVisible by remember { mutableStateOf(false) }
    var settingsVisible by remember { mutableStateOf(false) }
    var terminalVisible by remember { mutableStateOf(false) }
    var bitChatVisible by remember { mutableStateOf(false) }
    val settingsRepository = remember { SettingsRepository(context) }
    val apps = remember { getInstalledApps(context) }

    // Battery and RAM usage - updated once
    LaunchedEffect(context) {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus: Intent? = context.registerReceiver(null, intentFilter)
        val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        batteryLevel = (level * 100 / scale.toFloat()).toInt()

        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        ramUsage = (memoryInfo.totalMem - memoryInfo.availMem) / (1024 * 1024)
    }

    Column {
        Text(text = "RAM: $ramUsage MB")
        Text(text = "Battery: $batteryLevel%")
        Button(onClick = { appDrawerVisible = !appDrawerVisible }) {
            Text(text = "Apps")
        }
        Button(onClick = { settingsVisible = !settingsVisible }) {
            Text(text = "Settings")
        }
        Button(onClick = { terminalVisible = !terminalVisible }) {
            Text(text = "Terminal")
        }
        Button(onClick = { bitChatVisible = !bitChatVisible }) {
            Text(text = "BitChat")
        }
    }

    AnimatedVisibility(
        visible = appDrawerVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        AppDrawer(
            apps = apps,
            onAppClick = { appInfo ->
                val launchIntent = context.packageManager.getLaunchIntentForPackage(appInfo.packageName)
                context.startActivity(launchIntent)
            },
            onAppDragStart = { appInfo ->
                // TODO: Implement drag and drop
            }
        )
    }

    AnimatedVisibility(
        visible = settingsVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        SettingsScreen(settingsRepository = settingsRepository)
    }

    AnimatedVisibility(
        visible = terminalVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        TerminalView(apps = apps, onDismiss = { terminalVisible = false })
    }

    AnimatedVisibility(
        visible = bitChatVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        BitChatScreen()
    }
}

private fun getInstalledApps(context: Context): List<AppInfo> {
    val packageManager = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val resolveInfoList = packageManager.queryIntentActivities(intent, 0)
    return resolveInfoList.map { resolveInfo ->
        AppInfo(
            label = resolveInfo.loadLabel(packageManager).toString(),
            packageName = resolveInfo.activityInfo.packageName,
            icon = resolveInfo.loadIcon(packageManager)
        )
    }
}
