package com.aidaole.infuseandroid.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aidaole.infuseandroid.ui.widgets.ScreenTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        val uiState: Int = 0

        if (uiState == 0) {
            NoContentHome()
        } else {
            PosterScreen()
        }
    }
}

