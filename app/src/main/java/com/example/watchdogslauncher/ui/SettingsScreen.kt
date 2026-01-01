package com.example.watchdogslauncher.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.watchdogslauncher.data.SettingsRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settingsRepository: SettingsRepository) {
    val scope = rememberCoroutineScope()
    val settings by settingsRepository.settings.collectAsState(initial = com.example.watchdogslauncher.model.LauncherSettings())
    
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        // Theme Settings Section
        item {
            Text(
                "Theme Settings",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        item {
            ThemeColorPicker(
                currentTheme = settings.themeColor,
                onThemeSelected = { theme ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(themeColor = theme) }
                    }
                }
            )
        }
        
        item { Spacer(modifier = Modifier.height(8.dp)) }
        
        item {
            SliderSetting(
                label = "Animation Speed",
                value = settings.animationSpeed,
                valueRange = 0.5f..2.0f,
                onValueChange = { speed ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(animationSpeed = speed) }
                    }
                }
            )
        }
        
        item {
            SwitchSetting(
                label = "Enable Particle Effects",
                checked = settings.enableParticleEffects,
                onCheckedChange = { enabled ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(enableParticleEffects = enabled) }
                    }
                }
            )
        }
        
        item {
            DropdownSetting(
                label = "Background Effect",
                currentValue = settings.backgroundEffect,
                options = listOf("Grid", "Hexagonal", "Matrix", "Particles", "None"),
                onValueSelected = { effect ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(backgroundEffect = effect) }
                    }
                }
            )
        }
        
        // Home Screen Layout Section
        item {
            Text(
                "Home Screen Layout",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        
        item {
            SliderSetting(
                label = "Grid Rows: ${settings.gridRows}",
                value = settings.gridRows.toFloat(),
                valueRange = 3f..8f,
                steps = 4,
                onValueChange = { rows ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(gridRows = rows.toInt()) }
                    }
                }
            )
        }
        
        item {
            SliderSetting(
                label = "Grid Columns: ${settings.gridColumns}",
                value = settings.gridColumns.toFloat(),
                valueRange = 3f..6f,
                steps = 2,
                onValueChange = { cols ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(gridColumns = cols.toInt()) }
                    }
                }
            )
        }
        
        item {
            SliderSetting(
                label = "Icon Size",
                value = settings.iconSize,
                valueRange = 0.5f..2.0f,
                onValueChange = { size ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(iconSize = size) }
                    }
                }
            )
        }
        
        item {
            SwitchSetting(
                label = "Show App Labels",
                checked = settings.showLabels,
                onCheckedChange = { show ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(showLabels = show) }
                    }
                }
            )
        }
        
        // Gesture Settings Section
        item {
            Text(
                "Gesture Controls",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        
        val gestureActions = listOf("None", "AppDrawer", "Terminal", "Settings", "Notifications", "QuickSettings")
        
        item {
            DropdownSetting(
                label = "Swipe Up",
                currentValue = settings.gestureSwipeUp,
                options = gestureActions,
                onValueSelected = { action ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(gestureSwipeUp = action) }
                    }
                }
            )
        }
        
        item {
            DropdownSetting(
                label = "Swipe Down",
                currentValue = settings.gestureSwipeDown,
                options = gestureActions,
                onValueSelected = { action ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(gestureSwipeDown = action) }
                    }
                }
            )
        }
        
        item {
            DropdownSetting(
                label = "Double Tap",
                currentValue = settings.gestureDoubleTap,
                options = gestureActions,
                onValueSelected = { action ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(gestureDoubleTap = action) }
                    }
                }
            )
        }
        
        // Terminal Settings Section
        item {
            Text(
                "Terminal Settings",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        
        item {
            SwitchSetting(
                label = "Enable Autocomplete",
                checked = settings.terminalAutocomplete,
                onCheckedChange = { enabled ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(terminalAutocomplete = enabled) }
                    }
                }
            )
        }
        
        item {
            SliderSetting(
                label = "History Size: ${settings.terminalHistorySize}",
                value = settings.terminalHistorySize.toFloat(),
                valueRange = 10f..100f,
                steps = 17,
                onValueChange = { size ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(terminalHistorySize = size.toInt()) }
                    }
                }
            )
        }
        
        // Widget Settings Section
        item {
            Text(
                "Widget Settings",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        
        item {
            SwitchSetting(
                label = "Show System Stats",
                checked = settings.enableSystemStats,
                onCheckedChange = { enabled ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(enableSystemStats = enabled) }
                    }
                }
            )
        }
        
        item {
            SwitchSetting(
                label = "Show Clock",
                checked = settings.enableClock,
                onCheckedChange = { enabled ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(enableClock = enabled) }
                    }
                }
            )
        }
        
        item {
            DropdownSetting(
                label = "Clock Format",
                currentValue = settings.clockFormat,
                options = listOf("12h", "24h"),
                onValueSelected = { format ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(clockFormat = format) }
                    }
                }
            )
        }
        
        // Performance Settings Section
        item {
            Text(
                "Performance",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }
        
        item {
            SwitchSetting(
                label = "Enable Animations",
                checked = settings.enableAnimations,
                onCheckedChange = { enabled ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(enableAnimations = enabled) }
                    }
                }
            )
        }
        
        item {
            SwitchSetting(
                label = "Reduce Motion",
                checked = settings.reduceMotion,
                onCheckedChange = { enabled ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(reduceMotion = enabled) }
                    }
                }
            )
        }
        
        item {
            SwitchSetting(
                label = "Battery Optimization",
                checked = settings.batteryOptimization,
                onCheckedChange = { enabled ->
                    scope.launch {
                        settingsRepository.updateSettings { it.copy(batteryOptimization = enabled) }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeColorPicker(
    currentTheme: String,
    onThemeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val themeOptions = listOf(
        "HackerBlue", "HackerPurple", "GlitchPink", "CyberGreen", 
        "NeonPink", "ElectricBlue", "MatrixGreen", "TerminalAmber",
        "DarkPurple", "CrimsonRed", "TealCyan", "LimeGreen",
        "VividOrange", "DeepPink", "AquaBlue", "VioletPurple", "YellowGold"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Theme Color", color = MaterialTheme.colorScheme.onSurface)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                value = currentTheme,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                themeOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            onThemeSelected(selectionOption)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSetting(
    label: String,
    currentValue: String,
    options: List<String>,
    onValueSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurface)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                value = currentValue,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SliderSetting(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label, color = MaterialTheme.colorScheme.onSurface)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun SwitchSetting(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurface)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
        )
    }
}

