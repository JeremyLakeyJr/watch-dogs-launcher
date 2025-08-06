package com.example.watchdogslauncher.ui.homescreen

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppDrawer() {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN, null).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }
    val apps = packageManager.queryIntentActivities(intent, 0)

    LazyVerticalGrid(columns = GridCells.Fixed(4)) {
        items(apps) { app ->
            AppIcon(app, packageManager)
        }
    }
}

@Composable
fun AppIcon(app: ResolveInfo, packageManager: PackageManager) {
    val context = LocalContext.current
    val icon = app.loadIcon(packageManager)
    val label = app.loadLabel(packageManager).toString()

    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { val launchIntent = packageManager.getLaunchIntentForPackage(app.activityInfo.packageName)
                context.startActivity(launchIntent) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = rememberDrawablePainter(drawable = icon), contentDescription = label)
        Text(text = label)
    }
}
