package com.aidaole.infuseandroid.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aidaole.infuseandroid.ui.widgets.ScreenTitle


@Composable
fun SettingScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenTitle("设置")
    }
}