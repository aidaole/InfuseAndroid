package com.aidaole.infuseandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.aidaole.infuseandroid.ui.screen.main.MainScreen
import com.aidaole.infuseandroid.ui.theme.InfuseAndroidTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            InfuseAndroidTheme {
                ImmersiveScreen()
            }
        }
    }
}

@Composable
fun ImmersiveScreen() {
    // 获取系统栏的信息
    val systemUiController = rememberSystemUiController()
    val isDark = isSystemInDarkTheme()

    // 设置状态栏和导航栏
    SideEffect {
        // 设置状态栏颜色为透明
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = !isDark
        )

        // 设置导航栏颜色为透明
        systemUiController.setNavigationBarColor(
            color = Color.Transparent,
            darkIcons = !isDark
        )
    }
    InfuseApp()
}

@Composable
fun InfuseApp() {
    MainScreen()
}