package com.aidaole.infuseandroid.ui.screen.smb

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aidaole.infuseandroid.domain.model.FileItem
import com.aidaole.infuseandroid.domain.model.SmbServer
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SmbServiceScreen(
    viewModel: SmbServiceViewModel = hiltViewModel()
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var host by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()
    val servers by viewModel.servers.collectAsState()
    val currentPath by viewModel.currentPath.collectAsState()
    val files by viewModel.files.collectAsState()

    Log.d("SmbServiceScreen", "SmbServiceScreen: ${servers}")
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
            onClick = { showAddDialog = true }, modifier = Modifier.padding(bottom = 16.dp)
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
                        ServerItem(server = server,
                            onConnect = { viewModel.connectToServer(server) },
                            onRemove = { viewModel.removeServer(server.id) })
                    }

                    if (files.isNotEmpty()) {
                        item {
                            Text(
                                text = "Current Path: $currentPath",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        items(files) { item ->
                            FileItem(item = item, onItemClick = {
                                when (item) {
                                    is FileItem.Directory -> {
                                        viewModel.navigateToDirectory(
                                            servers.firstOrNull { it.isConnected }?.id
                                                ?: return@FileItem, item.path
                                        )
                                    }

                                    is FileItem.File -> {
                                        Log.d("SmbServiceScreen", "SmbServiceScreen: ${item.name}")
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(onDismissRequest = { showAddDialog = false },
            title = { Text("Add SMB Server") },
            text = {
                Column {
                    OutlinedTextField(value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = host,
                        onValueChange = { host = it },
                        label = { Text("Host (e.g. 192.168.1.100)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.addServer(name, host, username, password)
                    showAddDialog = false
                    name = ""
                    host = ""
                    username = ""
                    password = ""
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            })
    }
}

@Composable
fun ServerItem(
    server: SmbServer, onConnect: () -> Unit, onRemove: () -> Unit
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
                    text = server.name, style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = server.host, style = MaterialTheme.typography.bodyMedium
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

@Composable
fun FileItem(
    item: FileItem, onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), onClick = onItemClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (item) {
                    is FileItem.Directory -> Icons.Default.Add
                    is FileItem.File -> if (item.isVideo) Icons.Filled.Home else Icons.Default.Done
                }, contentDescription = null, modifier = Modifier.padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when (item) {
                        is FileItem.Directory -> item.name
                        is FileItem.File -> item.name
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = when (item) {
                        is FileItem.Directory -> "Directory"
                        is FileItem.File -> formatFileSize(item.size)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = when (item) {
                    is FileItem.Directory -> formatDate(item.lastModified)
                    is FileItem.File -> formatDate(item.lastModified)
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


private fun formatFileSize(size: Long): String {
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var fileSize = size.toDouble()
    var unitIndex = 0
    while (fileSize >= 1024 && unitIndex < units.size - 1) {
        fileSize /= 1024
        unitIndex++
    }
    return "%.2f %s".format(fileSize, units[unitIndex])
}

private fun formatDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
} 