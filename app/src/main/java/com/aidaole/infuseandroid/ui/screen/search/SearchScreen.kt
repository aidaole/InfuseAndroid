package com.aidaole.infuseandroid.ui.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aidaole.infuseandroid.ui.widgets.ScreenTitle

@Composable
fun SearchScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenTitle("Search")
    }
}