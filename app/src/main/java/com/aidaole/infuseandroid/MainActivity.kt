package com.aidaole.infuseandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.aidaole.infuseandroid.ui.components.InfuseBottomNavigation
import com.aidaole.infuseandroid.ui.navigation.InfuseNavHost
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
    val navController = rememberNavController()
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        InfuseNavHost(navController = navController)
        
        InfuseBottomNavigation(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            navController = navController
        )
    }
}