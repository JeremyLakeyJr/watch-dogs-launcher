
package com.example.watchdogslauncher.ui.homescreen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.watchdogslauncher.model.AppInfo
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppIcon(app: AppInfo) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .clickable {
                val launchIntent: Intent? = context.packageManager.getLaunchIntentForPackage(app.packageName.toString())
                if (launchIntent != null) {
                    context.startActivity(launchIntent)
                }
            }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberDrawablePainter(drawable = app.icon),
            contentDescription = app.label.toString(),
            modifier = Modifier.size(64.dp)
        )
        Text(text = app.label.toString(), maxLines = 1)
    }
}
