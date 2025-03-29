package com.aidaole.infuseandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aidaole.infuseandroid.ui.screen.main.MainScreen
import com.aidaole.infuseandroid.ui.theme.InfuseAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InfuseAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    InfuseApp()
                }
            }
        }
    }
}

@Composable
fun InfuseApp() {
    MainScreen()
}