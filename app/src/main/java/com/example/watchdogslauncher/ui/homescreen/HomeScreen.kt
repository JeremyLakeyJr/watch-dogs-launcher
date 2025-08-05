
package com.example.watchdogslauncher.ui.homescreen

import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.watchdogslauncher.model.AppInfo
import com.example.watchdogslauncher.ui.theme.WatchDogsLauncherTheme

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val apps = packageManager.getInstalledApplications(android.content.pm.PackageManager.GET_META_DATA)
        .map {
            AppInfo(
                label = it.loadLabel(packageManager),
                packageName = it.packageName,
                icon = it.loadIcon(packageManager)
            )
        }
        .sortedBy { it.label.toString() }

    Box(modifier = Modifier.fillMaxSize()) {
        HexagonalGrid()
        DataStreamOverlay()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Clock()
            AppList(apps = apps)
        }
    }
}

@Composable
fun AppList(apps: List<AppInfo>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(apps) { app ->
            StyledAppIcon(app = app)
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
