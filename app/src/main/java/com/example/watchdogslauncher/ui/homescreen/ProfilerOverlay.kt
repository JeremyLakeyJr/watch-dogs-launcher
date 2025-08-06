package com.example.watchdogslauncher.ui.homescreen

import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.watchdogslauncher.model.AppInfo

@Composable
fun ProfilerOverlay(appInfo: AppInfo, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val packageInfo = try {
        packageManager.getPackageInfo(appInfo.packageName.toString(), PackageManager.GET_PERMISSIONS)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
    val permissions = packageInfo?.requestedPermissions?.toList() ?: emptyList()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Profiler: ${appInfo.label}", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Permissions:", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                items(permissions) { permission ->
                    Text(
                        text = permission,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    }
}
