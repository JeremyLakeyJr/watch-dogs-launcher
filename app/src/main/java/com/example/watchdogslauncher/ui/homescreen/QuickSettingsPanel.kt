package com.example.watchdogslauncher.ui.homescreen

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class QuickSetting(
    val title: String,
    val icon: ImageVector,
    val action: String,
    val isToggle: Boolean = false
)

@Composable
fun QuickSettingsPanel(onDismiss: () -> Unit) {
    val context = LocalContext.current
    
    val quickSettings = remember {
        listOf(
            QuickSetting("Wi-Fi", Icons.Default.Wifi, Settings.ACTION_WIFI_SETTINGS),
            QuickSetting("Bluetooth", Icons.Default.Bluetooth, Settings.ACTION_BLUETOOTH_SETTINGS),
            QuickSetting("Location", Icons.Default.LocationOn, Settings.ACTION_LOCATION_SOURCE_SETTINGS),
            QuickSetting("Display", Icons.Default.Brightness6, Settings.ACTION_DISPLAY_SETTINGS),
            QuickSetting("Sound", Icons.Default.VolumeUp, Settings.ACTION_SOUND_SETTINGS),
            QuickSetting("Airplane", Icons.Default.AirplanemodeActive, Settings.ACTION_AIRPLANE_MODE_SETTINGS),
            QuickSetting("Battery", Icons.Default.BatteryChargingFull, Settings.ACTION_BATTERY_SAVER_SETTINGS),
            QuickSetting("Data", Icons.Default.DataUsage, Settings.ACTION_DATA_USAGE_SETTINGS),
            QuickSetting("Storage", Icons.Default.Storage, Settings.ACTION_INTERNAL_STORAGE_SETTINGS)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "QUICK SETTINGS",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Divider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Quick settings grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(quickSettings) { setting ->
                    QuickSettingTile(
                        setting = setting,
                        onClick = {
                            launchSettingIntent(context, setting.action)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun QuickSettingTile(
    setting: QuickSetting,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = setting.icon,
                contentDescription = setting.title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = setting.title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp
            )
        }
    }
}

private fun launchSettingIntent(context: Context, action: String) {
    try {
        val intent = Intent(action)
        if (intent.resolveActivity(context.packageManager) != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    } catch (e: Exception) {
        // Handle exception
    }
}
