package com.aidaole.infuseandroid.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        val uiState: Int = 0

        if (uiState == 0) {
            NoContentHome(navController)
        } else {
            PosterScreen()
        }
    }
}

