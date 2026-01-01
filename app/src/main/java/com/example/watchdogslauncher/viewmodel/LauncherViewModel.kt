package com.example.watchdogslauncher.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchdogslauncher.data.SettingsRepository
import com.example.watchdogslauncher.model.LauncherSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LauncherViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsRepository = SettingsRepository(application)
    
    private val _settings = MutableStateFlow(LauncherSettings())
    val settings: StateFlow<LauncherSettings> = _settings.asStateFlow()
    
    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _settings.value = settings
            }
        }
    }
    
    fun updateSettings(update: (LauncherSettings) -> LauncherSettings) {
        viewModelScope.launch {
            settingsRepository.updateSettings(update)
        }
    }
    
    fun setThemeColor(colorName: String) {
        updateSettings { it.copy(themeColor = colorName) }
    }
    
    fun setGridLayout(rows: Int, columns: Int) {
        updateSettings { it.copy(gridRows = rows, gridColumns = columns) }
    }
    
    fun setGestureAction(gesture: String, action: String) {
        updateSettings { settings ->
            when (gesture) {
                "swipeUp" -> settings.copy(gestureSwipeUp = action)
                "swipeDown" -> settings.copy(gestureSwipeDown = action)
                "swipeLeft" -> settings.copy(gestureSwipeLeft = action)
                "swipeRight" -> settings.copy(gestureSwipeRight = action)
                "doubleTap" -> settings.copy(gestureDoubleTap = action)
                "longPress" -> settings.copy(gestureLongPress = action)
                else -> settings
            }
        }
    }
    
    fun setBackgroundEffect(effect: String) {
        updateSettings { it.copy(backgroundEffect = effect) }
    }
    
    fun setAnimationSpeed(speed: Float) {
        updateSettings { it.copy(animationSpeed = speed) }
    }
    
    fun toggleParticleEffects() {
        updateSettings { it.copy(enableParticleEffects = !it.enableParticleEffects) }
    }
    
    fun toggleAnimations() {
        updateSettings { it.copy(enableAnimations = !it.enableAnimations) }
    }
    
    fun setIconSize(size: Float) {
        updateSettings { it.copy(iconSize = size) }
    }
    
    fun toggleLabels() {
        updateSettings { it.copy(showLabels = !it.showLabels) }
    }
}
