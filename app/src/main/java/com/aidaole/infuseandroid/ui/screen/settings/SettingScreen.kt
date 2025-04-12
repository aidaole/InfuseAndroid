package com.aidaole.infuseandroid.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aidaole.infuseandroid.ui.widgets.MainScreenTitle


@Composable
fun SettingScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MainScreenTitle("设置")
    }
}