
package com.example.watchdogslauncher.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.watchdogslauncher.model.IconPack

class IconPackManager(private val context: Context) {

    fun getInstalledIconPacks(): List<IconPack> {
        val iconPacks = mutableListOf<IconPack>()
        val packageManager = context.packageManager
        val intent = Intent("org.adw.launcher.THEMES")
        val resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.GET_META_DATA)
        for (info in resolveInfo) {
            val packageName = info.activityInfo.packageName
            val label = info.loadLabel(packageManager).toString()
            val icon = info.getIconResource()
            iconPacks.add(IconPack(packageName, label, icon))
        }
        return iconPacks
    }
}
