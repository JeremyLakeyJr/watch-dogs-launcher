package com.example.watchdogslauncher.ui.homescreen

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun SystemStatsWidget() {
    val context = LocalContext.current
    var batteryLevel by remember { mutableStateOf(0) }
    var ramUsage by remember { mutableStateOf(0L) }

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
    }
}
