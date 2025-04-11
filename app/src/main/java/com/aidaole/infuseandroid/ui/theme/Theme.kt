package com.aidaole.infuseandroid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Orange,
    onPrimary = Color.White,
    primaryContainer = OrangeLight,
    onPrimaryContainer = OrangeDark,
    
    secondary = Orange,
    onSecondary = Color.White,
    
    background = Background,
    onBackground = Color.Black,
    
    surface = Color.LightGray,
    onSurface = Color.Black,
    
    error = Error,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Orange,
    onPrimary = Color.Black,
    primaryContainer = OrangeLight,
    onPrimaryContainer = OrangeDark,
    
    secondary = Orange,
    onSecondary = Color.Black,
    
    background = Color(0xFF121212),
    onBackground = Color.White,
    
    surface = Color.DarkGray,
    onSurface = Color.White,
    
    error = Error,
    onError = Color.Black
)

@Composable
fun InfuseAndroidTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val materialColorScheme = if (useDarkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    // 扩展颜色属性
    val extendedColors = if (useDarkTheme) {
        DarkExtendedColors
    } else {
        LightExtendedColors
    }
    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = MaterialTheme.typography,
            shapes = MaterialTheme.shapes,
            content = content
        )
    }
}

object AppTheme {
    val extendedColors: ExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedColors.current
}