package com.aidaole.infuseandroid.ui.screen.servers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.Text
import com.aidaole.infuseandroid.data.repository.SmbRepositoryFake
import com.aidaole.infuseandroid.ui.widgets.SubScreenTitle

@Preview
@Composable
fun preivew() {
    AddSmbServerScreen(ServerManageViewModel(SmbRepositoryFake()))
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AddSmbServerScreen(
    serverManageViewModel: ServerManageViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var host by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.ArrowBackIosNew,
                contentDescription = "Back",
                Modifier
                    .clickable { }
                    .padding(10.dp)
            )
            SubScreenTitle(
                "添加 SMB", modifier = Modifier.weight(1F)
            )
        }

        HorizonInputWidget(
            name = "通讯协议",
            inputContent = "SMB",
            inputAble = false,
        )

        HorizonInputWidget(
            name = "名称",
            inputContent = "我的SMB",
            inputAble = true,
            onValueChange = { name = it })

        HorizonInputWidget(name = "ip",
            inputContent = "192.168.30.1",
            inputAble = true,
            onValueChange = { host = it })

        HorizonInputWidget(name = "用户名",
            inputContent = "johappessed",
            inputAble = true,
            onValueChange = { username = it })

        HorizonInputWidget(name = "密码",
            inputContent = "密码",
            inputAble = true,
            onValueChange = { password = it })

        Button(onClick = {
            serverManageViewModel.addServer(
                name, host, username, password
            )
        }) {
            Text("新增")
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun HorizonInputWidget(
    name: String = "名字",
    inputContent: String,
    inputAble: Boolean = true,
    onValueChange: (String) -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(2F)
        )
        if (inputAble) {
            OutlinedTextField(
                value = inputContent,
                modifier = Modifier.weight(5F),
                onValueChange = {
                    onValueChange(it)
                },
            )
        } else {
            Text(
                inputContent,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(5F)
                    .padding(vertical = 10.dp)
            )
        }

    }
}
