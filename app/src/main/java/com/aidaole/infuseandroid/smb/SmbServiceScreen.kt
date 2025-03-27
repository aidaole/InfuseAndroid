package com.aidaole.infuseandroid.smb

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aidaole.infuseandroid.domain.model.SmbServer

@Composable
fun SmbServiceScreen(
    viewModel: SmbServiceViewModel = hiltViewModel()
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("mac") }
    var host by remember { mutableStateOf("192.168.31.191") }
    var username by remember { mutableStateOf("yxm") }
    var password by remember { mutableStateOf("xiao") }

    val uiState by viewModel.uiState.collectAsState()
    val servers by viewModel.servers.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "SMB Servers",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Add Server")
        }

        when (uiState) {
            is SmbServiceUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is SmbServiceUiState.Error -> {
                Text(
                    text = (uiState as SmbServiceUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                LazyColumn {
                    items(servers) { server ->
                        ServerItem(
                            server = server,
                            onConnect = { viewModel.connectToServer(server) },
                            onRemove = { viewModel.removeServer(server.id) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add SMB Server") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = host,
                        onValueChange = { host = it },
                        label = { Text("Host (e.g. 192.168.1.100)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addServer(name, host, username, password)
                        showAddDialog = false
                        name = ""
                        host = ""
                        username = ""
                        password = ""
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ServerItem(
    server: SmbServer,
    onConnect: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = server.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = server.host,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row {
                Button(
                    onClick = onConnect,
                    enabled = !server.isConnected,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(if (server.isConnected) "Connected" else "Connect")
                }
                IconButton(onClick = onRemove) {
                    Text("×")
                }
            }
        }
    }
} 