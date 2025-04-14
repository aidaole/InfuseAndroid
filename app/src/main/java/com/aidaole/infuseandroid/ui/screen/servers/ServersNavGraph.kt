package com.aidaole.infuseandroid.ui.screen.servers

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aidaole.infuseandroid.data.entity.FavoriteFolderEntity
import com.aidaole.infuseandroid.data.model.FileItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ServersDestinations {
    const val ALL_SERVERS = "server_all"
    const val ADD_SMB_SERVER = "server_add"
    const val SERVER_FILES = "server_files"
}

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun ServersNavGraph(mainNavController: NavHostController) {

    val serverNavController = rememberNavController()
    val serverManageViewModel: ServerManageViewModel = hiltViewModel()

    NavHost(
        navController = serverNavController,
        startDestination = ServersDestinations.ALL_SERVERS
    ) {
        composable(ServersDestinations.ALL_SERVERS) {
            AllServerScreen(
                serverManageViewModel = serverManageViewModel,
                onAddServerClick = {
                    serverNavController.navigate(ServersDestinations.ADD_SMB_SERVER)
                },
                onServerItemClick = { server ->
                    serverManageViewModel.openServer(server, serverNavController)
                }
            )
        }
        composable(ServersDestinations.ADD_SMB_SERVER) {
            AddSmbServerScreen(
                serverManageViewModel = serverManageViewModel,
                onBackClick = { serverNavController.popBackStack() },
                onAddServerClick = { serverNavController.popBackStack() }
            )
        }
        composable(ServersDestinations.SERVER_FILES) {
            ServerFilesScreen(
                serverManageViewModel = serverManageViewModel,
                onOpenServerFailed = {
                    serverNavController.popBackStack()
                },
                onBackClick = { serverNavController.popBackStack() }
            )
        }
    }
}


@Composable
fun FavoriteFolderItem(
    folder: FavoriteFolderEntity, onItemClick: () -> Unit, onRemove: () -> Unit
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
            if (item is FileItem.Directory) {
                IconButton(onClick = onFavorite) {
                    Icon(Icons.Default.Star, contentDescription = "Favorite")
                }
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