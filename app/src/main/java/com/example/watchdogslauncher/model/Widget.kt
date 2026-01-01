package com.example.watchdogslauncher.model

import androidx.compose.ui.unit.IntOffset

sealed class WidgetType {
    object Clock : WidgetType()
    object SystemStats : WidgetType()
    object Weather : WidgetType()
    object Profiler : WidgetType()
    object QuickActions : WidgetType()
}

data class Widget(
    val id: String,
    val type: WidgetType,
    val position: IntOffset = IntOffset.Zero,
    val width: Int = 2, // Grid cells
    val height: Int = 1, // Grid cells
    val isVisible: Boolean = true
)

data class WidgetConfiguration(
    val widgets: List<Widget> = emptyList()
)
