package com.example.watchdogslauncher.model

data class LauncherSettings(
    // Theme Settings
    val themeColor: String = "HackerBlue",
    val customPrimaryColor: String? = null,
    val customAccentColor: String? = null,
    val fontFamily: String = "Default",
    val animationSpeed: Float = 1.0f,
    val enableParticleEffects: Boolean = true,
    val backgroundEffect: String = "Grid", // Grid, Hexagonal, Matrix, Particles
    
    // Home Screen Layout
    val gridRows: Int = 5,
    val gridColumns: Int = 4,
    val iconSize: Float = 1.0f,
    val showLabels: Boolean = true,
    
    // Gesture Settings
    val gestureSwipeUp: String = "AppDrawer",
    val gestureSwipeDown: String = "Notifications",
    val gestureSwipeLeft: String = "None",
    val gestureSwipeRight: String = "None",
    val gestureDoubleTap: String = "Terminal",
    val gestureLongPress: String = "None",
    
    // Terminal Settings
    val terminalHistorySize: Int = 50,
    val terminalAutocomplete: Boolean = true,
    val terminalTheme: String = "Dark",
    
    // Widget Settings
    val enableSystemStats: Boolean = true,
    val enableClock: Boolean = true,
    val clockFormat: String = "24h", // 24h or 12h
    val enableWeather: Boolean = false,
    
    // Advanced Settings
    val enableHapticFeedback: Boolean = true,
    val enableSoundEffects: Boolean = true,
    val soundEffectVolume: Float = 0.5f,
    val blurIntensity: Float = 0.3f,
    val uiTransparency: Float = 0.1f,
    
    // Icon Pack
    val iconPackEnabled: Boolean = false,
    val iconPackName: String? = null,
    
    // Performance
    val enableAnimations: Boolean = true,
    val reduceMotion: Boolean = false,
    val batteryOptimization: Boolean = false
)
