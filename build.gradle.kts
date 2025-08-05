// build.gradle.kts (Project-level)

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Use the latest stable version of the Android Gradle Plugin
    id("com.android.application") version "8.3.2" apply false
    // Your Kotlin version should be compatible with your Compose Compiler version (1.5.8)
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}
