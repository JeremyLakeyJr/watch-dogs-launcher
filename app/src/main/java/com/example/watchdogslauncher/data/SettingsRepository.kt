package com.example.watchdogslauncher.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.watchdogslauncher.model.LauncherSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    object PreferencesKeys {
        // Theme Settings
        val THEME_COLOR = stringPreferencesKey("theme_color")
        val CUSTOM_PRIMARY_COLOR = stringPreferencesKey("custom_primary_color")
        val CUSTOM_ACCENT_COLOR = stringPreferencesKey("custom_accent_color")
        val FONT_FAMILY = stringPreferencesKey("font_family")
        val ANIMATION_SPEED = floatPreferencesKey("animation_speed")
        val ENABLE_PARTICLE_EFFECTS = booleanPreferencesKey("enable_particle_effects")
        val BACKGROUND_EFFECT = stringPreferencesKey("background_effect")
        
        // Home Screen Layout
        val GRID_ROWS = intPreferencesKey("grid_rows")
        val GRID_COLUMNS = intPreferencesKey("grid_columns")
        val ICON_SIZE = floatPreferencesKey("icon_size")
        val SHOW_LABELS = booleanPreferencesKey("show_labels")
        
        // Gesture Settings
        val GESTURE_SWIPE_UP = stringPreferencesKey("gesture_swipe_up")
        val GESTURE_SWIPE_DOWN = stringPreferencesKey("gesture_swipe_down")
        val GESTURE_SWIPE_LEFT = stringPreferencesKey("gesture_swipe_left")
        val GESTURE_SWIPE_RIGHT = stringPreferencesKey("gesture_swipe_right")
        val GESTURE_DOUBLE_TAP = stringPreferencesKey("gesture_double_tap")
        val GESTURE_LONG_PRESS = stringPreferencesKey("gesture_long_press")
        
        // Terminal Settings
        val TERMINAL_HISTORY_SIZE = intPreferencesKey("terminal_history_size")
        val TERMINAL_AUTOCOMPLETE = booleanPreferencesKey("terminal_autocomplete")
        val TERMINAL_THEME = stringPreferencesKey("terminal_theme")
        
        // Widget Settings
        val ENABLE_SYSTEM_STATS = booleanPreferencesKey("enable_system_stats")
        val ENABLE_CLOCK = booleanPreferencesKey("enable_clock")
        val CLOCK_FORMAT = stringPreferencesKey("clock_format")
        val ENABLE_WEATHER = booleanPreferencesKey("enable_weather")
        
        // Advanced Settings
        val ENABLE_HAPTIC_FEEDBACK = booleanPreferencesKey("enable_haptic_feedback")
        val ENABLE_SOUND_EFFECTS = booleanPreferencesKey("enable_sound_effects")
        val SOUND_EFFECT_VOLUME = floatPreferencesKey("sound_effect_volume")
        val BLUR_INTENSITY = floatPreferencesKey("blur_intensity")
        val UI_TRANSPARENCY = floatPreferencesKey("ui_transparency")
        
        // Icon Pack
        val ICON_PACK_ENABLED = booleanPreferencesKey("icon_pack_enabled")
        val ICON_PACK_NAME = stringPreferencesKey("icon_pack_name")
        
        // Performance
        val ENABLE_ANIMATIONS = booleanPreferencesKey("enable_animations")
        val REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
        val BATTERY_OPTIMIZATION = booleanPreferencesKey("battery_optimization")
    }

    val settings: Flow<LauncherSettings> = context.dataStore.data
        .map { preferences ->
            LauncherSettings(
                themeColor = preferences[PreferencesKeys.THEME_COLOR] ?: "HackerBlue",
                customPrimaryColor = preferences[PreferencesKeys.CUSTOM_PRIMARY_COLOR],
                customAccentColor = preferences[PreferencesKeys.CUSTOM_ACCENT_COLOR],
                fontFamily = preferences[PreferencesKeys.FONT_FAMILY] ?: "Default",
                animationSpeed = preferences[PreferencesKeys.ANIMATION_SPEED] ?: 1.0f,
                enableParticleEffects = preferences[PreferencesKeys.ENABLE_PARTICLE_EFFECTS] ?: true,
                backgroundEffect = preferences[PreferencesKeys.BACKGROUND_EFFECT] ?: "Grid",
                
                gridRows = preferences[PreferencesKeys.GRID_ROWS] ?: 5,
                gridColumns = preferences[PreferencesKeys.GRID_COLUMNS] ?: 4,
                iconSize = preferences[PreferencesKeys.ICON_SIZE] ?: 1.0f,
                showLabels = preferences[PreferencesKeys.SHOW_LABELS] ?: true,
                
                gestureSwipeUp = preferences[PreferencesKeys.GESTURE_SWIPE_UP] ?: "AppDrawer",
                gestureSwipeDown = preferences[PreferencesKeys.GESTURE_SWIPE_DOWN] ?: "Notifications",
                gestureSwipeLeft = preferences[PreferencesKeys.GESTURE_SWIPE_LEFT] ?: "None",
                gestureSwipeRight = preferences[PreferencesKeys.GESTURE_SWIPE_RIGHT] ?: "None",
                gestureDoubleTap = preferences[PreferencesKeys.GESTURE_DOUBLE_TAP] ?: "Terminal",
                gestureLongPress = preferences[PreferencesKeys.GESTURE_LONG_PRESS] ?: "None",
                
                terminalHistorySize = preferences[PreferencesKeys.TERMINAL_HISTORY_SIZE] ?: 50,
                terminalAutocomplete = preferences[PreferencesKeys.TERMINAL_AUTOCOMPLETE] ?: true,
                terminalTheme = preferences[PreferencesKeys.TERMINAL_THEME] ?: "Dark",
                
                enableSystemStats = preferences[PreferencesKeys.ENABLE_SYSTEM_STATS] ?: true,
                enableClock = preferences[PreferencesKeys.ENABLE_CLOCK] ?: true,
                clockFormat = preferences[PreferencesKeys.CLOCK_FORMAT] ?: "24h",
                enableWeather = preferences[PreferencesKeys.ENABLE_WEATHER] ?: false,
                
                enableHapticFeedback = preferences[PreferencesKeys.ENABLE_HAPTIC_FEEDBACK] ?: true,
                enableSoundEffects = preferences[PreferencesKeys.ENABLE_SOUND_EFFECTS] ?: true,
                soundEffectVolume = preferences[PreferencesKeys.SOUND_EFFECT_VOLUME] ?: 0.5f,
                blurIntensity = preferences[PreferencesKeys.BLUR_INTENSITY] ?: 0.3f,
                uiTransparency = preferences[PreferencesKeys.UI_TRANSPARENCY] ?: 0.1f,
                
                iconPackEnabled = preferences[PreferencesKeys.ICON_PACK_ENABLED] ?: false,
                iconPackName = preferences[PreferencesKeys.ICON_PACK_NAME],
                
                enableAnimations = preferences[PreferencesKeys.ENABLE_ANIMATIONS] ?: true,
                reduceMotion = preferences[PreferencesKeys.REDUCE_MOTION] ?: false,
                batteryOptimization = preferences[PreferencesKeys.BATTERY_OPTIMIZATION] ?: false
            )
        }

    // Legacy support for existing code
    val themeColor: Flow<String> = settings.map { it.themeColor }

    suspend fun updateSettings(update: (LauncherSettings) -> LauncherSettings) {
        context.dataStore.edit { preferences ->
            val current = LauncherSettings(
                themeColor = preferences[PreferencesKeys.THEME_COLOR] ?: "HackerBlue",
                customPrimaryColor = preferences[PreferencesKeys.CUSTOM_PRIMARY_COLOR],
                customAccentColor = preferences[PreferencesKeys.CUSTOM_ACCENT_COLOR],
                fontFamily = preferences[PreferencesKeys.FONT_FAMILY] ?: "Default",
                animationSpeed = preferences[PreferencesKeys.ANIMATION_SPEED] ?: 1.0f,
                enableParticleEffects = preferences[PreferencesKeys.ENABLE_PARTICLE_EFFECTS] ?: true,
                backgroundEffect = preferences[PreferencesKeys.BACKGROUND_EFFECT] ?: "Grid",
                
                gridRows = preferences[PreferencesKeys.GRID_ROWS] ?: 5,
                gridColumns = preferences[PreferencesKeys.GRID_COLUMNS] ?: 4,
                iconSize = preferences[PreferencesKeys.ICON_SIZE] ?: 1.0f,
                showLabels = preferences[PreferencesKeys.SHOW_LABELS] ?: true,
                
                gestureSwipeUp = preferences[PreferencesKeys.GESTURE_SWIPE_UP] ?: "AppDrawer",
                gestureSwipeDown = preferences[PreferencesKeys.GESTURE_SWIPE_DOWN] ?: "Notifications",
                gestureSwipeLeft = preferences[PreferencesKeys.GESTURE_SWIPE_LEFT] ?: "None",
                gestureSwipeRight = preferences[PreferencesKeys.GESTURE_SWIPE_RIGHT] ?: "None",
                gestureDoubleTap = preferences[PreferencesKeys.GESTURE_DOUBLE_TAP] ?: "Terminal",
                gestureLongPress = preferences[PreferencesKeys.GESTURE_LONG_PRESS] ?: "None",
                
                terminalHistorySize = preferences[PreferencesKeys.TERMINAL_HISTORY_SIZE] ?: 50,
                terminalAutocomplete = preferences[PreferencesKeys.TERMINAL_AUTOCOMPLETE] ?: true,
                terminalTheme = preferences[PreferencesKeys.TERMINAL_THEME] ?: "Dark",
                
                enableSystemStats = preferences[PreferencesKeys.ENABLE_SYSTEM_STATS] ?: true,
                enableClock = preferences[PreferencesKeys.ENABLE_CLOCK] ?: true,
                clockFormat = preferences[PreferencesKeys.CLOCK_FORMAT] ?: "24h",
                enableWeather = preferences[PreferencesKeys.ENABLE_WEATHER] ?: false,
                
                enableHapticFeedback = preferences[PreferencesKeys.ENABLE_HAPTIC_FEEDBACK] ?: true,
                enableSoundEffects = preferences[PreferencesKeys.ENABLE_SOUND_EFFECTS] ?: true,
                soundEffectVolume = preferences[PreferencesKeys.SOUND_EFFECT_VOLUME] ?: 0.5f,
                blurIntensity = preferences[PreferencesKeys.BLUR_INTENSITY] ?: 0.3f,
                uiTransparency = preferences[PreferencesKeys.UI_TRANSPARENCY] ?: 0.1f,
                
                iconPackEnabled = preferences[PreferencesKeys.ICON_PACK_ENABLED] ?: false,
                iconPackName = preferences[PreferencesKeys.ICON_PACK_NAME],
                
                enableAnimations = preferences[PreferencesKeys.ENABLE_ANIMATIONS] ?: true,
                reduceMotion = preferences[PreferencesKeys.REDUCE_MOTION] ?: false,
                batteryOptimization = preferences[PreferencesKeys.BATTERY_OPTIMIZATION] ?: false
            )
            
            val updated = update(current)
            
            preferences[PreferencesKeys.THEME_COLOR] = updated.themeColor
            updated.customPrimaryColor?.let { preferences[PreferencesKeys.CUSTOM_PRIMARY_COLOR] = it }
            updated.customAccentColor?.let { preferences[PreferencesKeys.CUSTOM_ACCENT_COLOR] = it }
            preferences[PreferencesKeys.FONT_FAMILY] = updated.fontFamily
            preferences[PreferencesKeys.ANIMATION_SPEED] = updated.animationSpeed
            preferences[PreferencesKeys.ENABLE_PARTICLE_EFFECTS] = updated.enableParticleEffects
            preferences[PreferencesKeys.BACKGROUND_EFFECT] = updated.backgroundEffect
            
            preferences[PreferencesKeys.GRID_ROWS] = updated.gridRows
            preferences[PreferencesKeys.GRID_COLUMNS] = updated.gridColumns
            preferences[PreferencesKeys.ICON_SIZE] = updated.iconSize
            preferences[PreferencesKeys.SHOW_LABELS] = updated.showLabels
            
            preferences[PreferencesKeys.GESTURE_SWIPE_UP] = updated.gestureSwipeUp
            preferences[PreferencesKeys.GESTURE_SWIPE_DOWN] = updated.gestureSwipeDown
            preferences[PreferencesKeys.GESTURE_SWIPE_LEFT] = updated.gestureSwipeLeft
            preferences[PreferencesKeys.GESTURE_SWIPE_RIGHT] = updated.gestureSwipeRight
            preferences[PreferencesKeys.GESTURE_DOUBLE_TAP] = updated.gestureDoubleTap
            preferences[PreferencesKeys.GESTURE_LONG_PRESS] = updated.gestureLongPress
            
            preferences[PreferencesKeys.TERMINAL_HISTORY_SIZE] = updated.terminalHistorySize
            preferences[PreferencesKeys.TERMINAL_AUTOCOMPLETE] = updated.terminalAutocomplete
            preferences[PreferencesKeys.TERMINAL_THEME] = updated.terminalTheme
            
            preferences[PreferencesKeys.ENABLE_SYSTEM_STATS] = updated.enableSystemStats
            preferences[PreferencesKeys.ENABLE_CLOCK] = updated.enableClock
            preferences[PreferencesKeys.CLOCK_FORMAT] = updated.clockFormat
            preferences[PreferencesKeys.ENABLE_WEATHER] = updated.enableWeather
            
            preferences[PreferencesKeys.ENABLE_HAPTIC_FEEDBACK] = updated.enableHapticFeedback
            preferences[PreferencesKeys.ENABLE_SOUND_EFFECTS] = updated.enableSoundEffects
            preferences[PreferencesKeys.SOUND_EFFECT_VOLUME] = updated.soundEffectVolume
            preferences[PreferencesKeys.BLUR_INTENSITY] = updated.blurIntensity
            preferences[PreferencesKeys.UI_TRANSPARENCY] = updated.uiTransparency
            
            preferences[PreferencesKeys.ICON_PACK_ENABLED] = updated.iconPackEnabled
            updated.iconPackName?.let { preferences[PreferencesKeys.ICON_PACK_NAME] = it }
            
            preferences[PreferencesKeys.ENABLE_ANIMATIONS] = updated.enableAnimations
            preferences[PreferencesKeys.REDUCE_MOTION] = updated.reduceMotion
            preferences[PreferencesKeys.BATTERY_OPTIMIZATION] = updated.batteryOptimization
        }
    }

    // Legacy support
    suspend fun setThemeColor(colorName: String) {
        updateSettings { it.copy(themeColor = colorName) }
    }
}
