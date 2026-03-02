package org.vocaminder

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.compositionLocalOf

val LocalWindowSizeClass = compositionLocalOf<WindowSizeClass> { error("SizeClass not present") }

val LocalPlatform = compositionLocalOf<Platform> { error("LocalPlatform not set") }

// enum class Platform {
//    IOS,
//    Android
//}
