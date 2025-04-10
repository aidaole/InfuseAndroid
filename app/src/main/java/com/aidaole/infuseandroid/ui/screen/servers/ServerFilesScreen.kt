package com.aidaole.infuseandroid.ui.screen.servers

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ServerFilesScreen(
    serversManageViewModel: ServersManageViewModel = hiltViewModel()
) {

    Column {
        Text(
            "文件夹",
            fontStyle = MaterialTheme.typography.titleLarge.fontStyle
        )

    }

}