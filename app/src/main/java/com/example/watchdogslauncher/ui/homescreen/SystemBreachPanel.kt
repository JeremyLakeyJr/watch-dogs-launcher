package com.example.watchdogslauncher.ui.homescreen

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.NetworkWifi
import androidx.compose.material.icons.filled.SettingsBluetooth
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// Data class to better structure your settings items
private data class SettingsItem(
    val title: String,
    val action: String,
    val icon: ImageVector
)

@Composable
fun SystemBreachPanel() {
    val context = LocalContext.current

    // A list of SettingsItem, each with a specific icon
    val settingsItems = listOf(
        SettingsItem("Wi-Fi Settings", Settings.ACTION_WIFI_SETTINGS, Icons.Default.NetworkWifi),
        SettingsItem("Bluetooth Settings", Settings.ACTION_BLUETOOTH_SETTINGS, Icons.Default.SettingsBluetooth),
        SettingsItem("Display Settings", Settings.ACTION_DISPLAY_SETTINGS, Icons.Default.Wallpaper),
        SettingsItem("Sound Settings", Settings.ACTION_SOUND_SETTINGS, Icons.Default.VolumeUp),
        SettingsItem("Apps Settings", Settings.ACTION_APPLICATION_SETTINGS, Icons.Default.Apps),
        SettingsItem("Storage Settings", Settings.ACTION_INTERNAL_STORAGE_SETTINGS, Icons.Default.Storage)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("System Breach", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.padding(8.dp))

        settingsItems.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        val intent = Intent(item.action)
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        }
                    }
            ) {
                Icon(imageVector = item.icon, contentDescription = item.title, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = item.title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}
