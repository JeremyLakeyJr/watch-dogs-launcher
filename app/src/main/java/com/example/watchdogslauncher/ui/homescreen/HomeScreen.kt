
package com.example.watchdogslauncher.ui.homescreen

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.watchdogslauncher.R
import com.example.watchdogslauncher.SettingsActivity
import com.example.watchdogslauncher.model.AppInfo
import com.example.watchdogslauncher.ui.appdrawer.AppDrawer
import com.example.watchdogslauncher.ui.terminal.Terminal
import com.example.watchdogslauncher.ui.theme.WatchDogsLauncherTheme

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        .map {
            AppInfo(
                label = it.loadLabel(packageManager),
                packageName = it.packageName,
                icon = it.loadIcon(packageManager)
            )
        }
        .sortedBy { it.label.toString() }

    var showAppDrawer by remember { mutableStateOf(false) }
    var showTerminal by remember { mutableStateOf(false) }
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.hack_sound) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    if (dragAmount.y < -100) {
                        showAppDrawer = true
                        mediaPlayer.start()
                    }
                }
            }
    ) {
        HexagonalGrid()
        DataStreamOverlay()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SystemStatsWidget()
            Clock()
            TargetingSystem(apps = apps)
        }
        if (showAppDrawer) {
            AppDrawer()
        }
        if (showTerminal) {
            Terminal()
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = android.R.drawable.ic_dialog_info),
                contentDescription = "Terminal",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        showTerminal = !showTerminal
                    }
            )
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_preferences),
                contentDescription = "Settings",
                modifier = Modifier
                    .clickable {
                        context.startActivity(Intent(context, SettingsActivity::class.java))
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    WatchDogsLauncherTheme {
        HomeScreen()
    }
}
