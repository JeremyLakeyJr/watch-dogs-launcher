
package com.example.watchdogslauncher.ui.appdrawer

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.watchdogslauncher.model.AppInfo
import com.example.watchdogslauncher.ui.homescreen.StyledAppIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer() {
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

    var searchQuery by remember { mutableStateOf("") }

    val filteredApps = if (searchQuery.isEmpty()) {
        apps
    } else {
        apps.filter { it.label.toString().contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search") }
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp)
        ) {
            items(filteredApps) { app ->
                StyledAppIcon(app = app)
            }
        }
    }
}
