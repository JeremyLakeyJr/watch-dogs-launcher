package com.example.watchdogslauncher.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    object PreferencesKeys {
        val THEME_COLOR = stringPreferencesKey("theme_color")
    }

    val themeColor: Flow<String> = context.dataStore.data
        .map {
            it[PreferencesKeys.THEME_COLOR] ?: "HackerBlue"
        }

    suspend fun setThemeColor(colorName: String) {
        context.dataStore.edit {
            it[PreferencesKeys.THEME_COLOR] = colorName
        }
    }
}
