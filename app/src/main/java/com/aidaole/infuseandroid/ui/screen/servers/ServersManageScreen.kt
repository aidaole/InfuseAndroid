package com.aidaole.infuseandroid.ui.screen.servers

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aidaole.infuseandroid.R
import com.aidaole.infuseandroid.data.model.FavoriteFolder
import com.aidaole.infuseandroid.data.model.FileItem
import com.aidaole.infuseandroid.data.model.SmbServer
import com.aidaole.infuseandroid.ui.screen.servers.widgets.AddServerItem
import com.aidaole.infuseandroid.ui.theme.DividerColor
import com.aidaole.infuseandroid.ui.widgets.ScreenTitle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SmbServiceScreen(
    viewModel: ServersManageViewModel = hiltViewModel()
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
    val favoriteFolders by viewModel.favoriteFolders.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenTitle(text = "服务器")
        AddServerItems { id ->
            showAddDialog = true
        }
        Spacer(modifier = Modifier.height(40.dp))

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
                    item {
                        Text(
                            text = "已连接的服务器",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    items(servers) { server ->
                        ServerItemView(server = server,
                            onItemClick = { viewModel.openServer(server) },
                            onRemoveClick = { viewModel.removeServer(server.id) })
                    }

                    if (favoriteFolders.isNotEmpty()) {
                        item {
                            Text(
                                text = "已存储的共享",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        items(favoriteFolders) { folder ->
                            FavoriteFolderItem(folder = folder,
                                onItemClick = { viewModel.navigateToDirectory(folder.serverId, folder.path) },
                                onRemove = { viewModel.removeFavoriteFolder(folder.id) })
                        }
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
                                            servers.firstOrNull { it.isConnected }?.id ?: return@FileItem, item.path
                                        )
                                    }

                                    is FileItem.File -> {
                                        Log.d("SmbServiceScreen", "SmbServiceScreen: ${item.name}")
                                    }
                                }
                            }, onFavorite = {
                                if (item is FileItem.Directory) {
                                    viewModel.addFavoriteFolder(
                                        servers.firstOrNull { it.isConnected }?.id ?: return@FileItem,
                                        item.path,
                                        item.name
                                    )
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(onDismissRequest = { showAddDialog = false }, title = { Text("Add SMB Server") }, text = {
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
        }, confirmButton = {
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
        }, dismissButton = {
            TextButton(onClick = { showAddDialog = false }) {
                Text("Cancel")
            }
        })
    }
}

@Composable
private fun AddServerItems(
    onItemClick: (id: Int) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        AddServerItem(text = "DiskStation(SMB)", serverIcon = painterResource(R.drawable.ic_smb), onClicked = {
            onItemClick(0)
        })
        AddServerItem(text = "Aliyun", serverIcon = painterResource(R.drawable.ic_aliyun), onClicked = {
            onItemClick(1)
        })
        AddServerItem("Box", serverIcon = painterResource(R.drawable.ic_box), onClicked = {
            onItemClick(2)
        })
    }
}

@Composable
fun ServerItemView(
    server: SmbServer, onItemClick: () -> Unit, onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clickable { onItemClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = DividerColor)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = server.name, style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = server.host, style = MaterialTheme.typography.bodyMedium
                )
            }
            Row {
                IconButton(onClick = onRemoveClick) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteFolderItem(
    folder: FavoriteFolder, onItemClick: () -> Unit, onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp), onClick = onItemClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = folder.name, style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = folder.path,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
            }
        }
    }
}

@Composable
fun FileItem(
    item: FileItem, onItemClick: () -> Unit, onFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), onClick = onItemClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (item) {
                    is FileItem.Directory -> Icons.Default.Folder
                    is FileItem.File -> if (item.isVideo) Icons.Default.VideoFile else Icons.Default.InsertDriveFile
                }, contentDescription = null, modifier = Modifier.padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when (item) {
                        is FileItem.Directory -> item.name
                        is FileItem.File -> item.name
                    }, style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = when (item) {
                        is FileItem.Directory -> "Directory"
                        is FileItem.File -> formatFileSize(item.size)
                    }, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (item is FileItem.Directory) {
                IconButton(onClick = onFavorite) {
                    Icon(Icons.Default.Star, contentDescription = "Favorite")
                }
            }
            Text(
                text = when (item) {
                    is FileItem.Directory -> formatDate(item.lastModified)
                    is FileItem.File -> formatDate(item.lastModified)
                }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant
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