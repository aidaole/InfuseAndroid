package com.aidaole.infuseandroid.ui.screen.servers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aidaole.infuseandroid.ui.widgets.SubTitleActionBar

@Composable
fun ServerFilesScreen(
    serverManageViewModel: ServerManageViewModel,
    onOpenServerFailed: () -> Unit,
    onBackClick: () -> Unit
) {

    val smbServer by serverManageViewModel.selectSmbServer.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (smbServer == null) {
            onOpenServerFailed()
            Column {
                Text("服务器连接失败")
            }
        } else {
            SubTitleActionBar(
                title = smbServer!!.name,
                onBackClick = {
                    onBackClick.invoke()
                }
            )
            Text(
                "文件夹", fontStyle = MaterialTheme.typography.titleLarge.fontStyle
            )
        }
    }

}