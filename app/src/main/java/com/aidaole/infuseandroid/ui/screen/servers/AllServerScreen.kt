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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aidaole.infuseandroid.R
import com.aidaole.infuseandroid.data.model.FileItem
import com.aidaole.infuseandroid.data.model.SmbServer
import com.aidaole.infuseandroid.ui.screen.servers.widgets.AddServerItem
import com.aidaole.infuseandroid.ui.theme.AppTheme
import com.aidaole.infuseandroid.ui.widgets.MainScreenTitle


@Composable
fun AllServerScreen(
    serverManageViewModel: ServerManageViewModel,
    onAddServerClick: (Int) -> Unit,
    onServerItemClick: (SmbServer) -> Unit
) {
    val uiState by serverManageViewModel.uiState.collectAsState()
    val servers by serverManageViewModel.servers.collectAsState()
    val currentPath by serverManageViewModel.currentPath.collectAsState()
    val files by serverManageViewModel.files.collectAsState()
    val favoriteFolders by serverManageViewModel.favoriteFolders.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MainScreenTitle(text = "服务器")
        AddServerItems { id ->
            onAddServerClick(id)
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
                    item {
                        Spacer(Modifier.height(16.dp))
                    }
                    item {
                        Text(
                            text = "已连接的服务器",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    item {
                        Spacer(Modifier.height(16.dp))
                    }
                    items(servers) { server ->
                        ServerItemView(
                            server = server,
                            onItemClick = {
                                onServerItemClick.invoke(server)
                            },
                            onRemoveClick = { serverManageViewModel.removeServer(server.id) })
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
                                onItemClick = {
                                    serverManageViewModel.navigateToDirectory(
                                        folder.serverId, folder.path
                                    )
                                },
                                onRemove = { serverManageViewModel.removeFavoriteFolder(folder.id) })
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
                                        serverManageViewModel.navigateToDirectory(
                                            servers.firstOrNull { it.isConnected }?.id
                                                ?: return@FileItem, item.path
                                        )
                                    }

                                    is FileItem.File -> {
                                        Log.d("SmbServiceScreen", "SmbServiceScreen: ${item.name}")
                                    }
                                }
                            }, onFavorite = {
                                if (item is FileItem.Directory) {
                                    serverManageViewModel.addFavoriteFolder(
                                        servers.firstOrNull { it.isConnected }?.id
                                            ?: return@FileItem, item.path, item.name
                                    )
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddServerItems(
    onItemClick: (id: Int) -> Unit
) {
    Text(
        text = "网络共享",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(16.dp)
    )
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(
                color = AppTheme.extendedColors.addServerItem, shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
    ) {
        AddServerItem(text = "添加 SMB",
            serverIcon = painterResource(R.drawable.ic_smb),
            onClicked = {
                onItemClick(0)
            })
        AddServerItem(text = "添加 FTP",
            serverIcon = painterResource(R.drawable.ic_smb),
            onClicked = {
                onItemClick(0)
            })
        AddServerItem(text = "添加 NFS",
            showDivider = false,
            serverIcon = painterResource(R.drawable.ic_smb),
            onClicked = {
                onItemClick(0)
            })
    }
    Text(
        text = "云端服务",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(16.dp)
    )
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(
                color = AppTheme.extendedColors.addServerItem, shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
    ) {
        AddServerItem(text = "Aliyun",
            serverIcon = painterResource(R.drawable.ic_aliyun),
            onClicked = {
                onItemClick(1)
            })
        AddServerItem("Box",
            showDivider = false,
            serverIcon = painterResource(R.drawable.ic_box),
            onClicked = {
                onItemClick(2)
            })
    }
}

@Composable
fun ServerItemView(
    server: SmbServer, onItemClick: () -> Unit, onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(color = AppTheme.extendedColors.addServerItem)
            .clickable { onItemClick() }
            .clip(RoundedCornerShape(10.dp))
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
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


