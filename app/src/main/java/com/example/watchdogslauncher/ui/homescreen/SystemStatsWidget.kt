
package com.example.watchdogslauncher.ui.homescreen

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.io.RandomAccessFile
import java.util.regex.Pattern

@Composable
fun SystemStatsWidget() {
    val context = LocalContext.current
    var batteryLevel by remember { mutableStateOf(0) }
    var ramUsage by remember { mutableStateOf(0L) }
    var cpuUsage by remember { mutableStateOf(0f) }

    DisposableEffect(context) {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus: Intent? = context.registerReceiver(null, intentFilter)
        val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        batteryLevel = (level * 100 / scale.toFloat()).toInt()

        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        ramUsage = (memoryInfo.totalMem - memoryInfo.availMem) / (1024 * 1024)

        val reader = RandomAccessFile("/proc/stat", "r")
        val load = reader.readLine()
        val toks = load.split(" ")
        val idle1 = toks[4].toLong()
        val cpu1 = toks[2].toLong() + toks[3].toLong() + toks[5].toLong() + toks[6].toLong() + toks[7].toLong() + toks[8].toLong()
        try {
            Thread.sleep(360)
        } catch (e: Exception) {
        }
        reader.seek(0)
        val load2 = reader.readLine()
        reader.close()
        val toks2 = load2.split(" ")
        val idle2 = toks2[4].toLong()
        val cpu2 = toks2[2].toLong() + toks2[3].toLong() + toks2[5].toLong() + toks2[6].toLong() + toks2[7].toLong() + toks2[8].toLong()
        cpuUsage = (cpu2 - cpu1).toFloat() / ((cpu2 + idle2) - (cpu1 + idle1)) * 100


        onDispose { }
    }

    Column {
        Text(text = "CPU: ${"%.2f".format(cpuUsage)}%")
        Text(text = "RAM: $ramUsage MB")
        Text(text = "Battery: $batteryLevel%")
    }
}
