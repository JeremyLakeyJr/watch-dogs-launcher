
package com.example.watchdogslauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.watchdogslauncher.ui.theme.WatchDogsLauncherTheme
import com.example.watchdogslauncher.util.IconPackManager

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchDogsLauncherTheme {
                val iconPackManager = IconPackManager(this)
                val iconPacks = iconPackManager.getInstalledIconPacks()
                Column {
                    Text(text = "Settings")
                    LazyColumn {
                        items(iconPacks) { iconPack ->
                            Text(text = iconPack.label)
                        }
                    }
                }
            }
        }
    }
}
