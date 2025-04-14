package com.aidaole.infuseandroid.ui.screen.servers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.aidaole.infuseandroid.ui.widgets.MainScreenTitle

@Composable
fun ServerFilesScreen(
    serverManageViewModel: ServerManageViewModel, onOpenServerFailed: () -> Unit
) {

    val smbServer by serverManageViewModel.selectSmbServer.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (smbServer == null) {
            onOpenServerFailed()
            Column {
                Text("服务器连接失败")
            }
        } else {
            Column {
                MainScreenTitle("服务器: ${smbServer?.name}")
                Text(
                    "文件夹", fontStyle = MaterialTheme.typography.titleLarge.fontStyle
                )

            }
        }
    }

}