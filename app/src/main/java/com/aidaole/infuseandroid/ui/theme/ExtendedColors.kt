package com.aidaole.infuseandroid.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    val addServerItem: Color,
)

val LocalExtendedColors = staticCompositionLocalOf<ExtendedColors> {
    // 默认值，如果忘记提供，会 crash（可选）
    error("No ExtendedColors provided")
}

val LightExtendedColors = ExtendedColors(
    addServerItem = Color.White
)

val DarkExtendedColors = ExtendedColors(
    addServerItem = Color.DarkGray,
)