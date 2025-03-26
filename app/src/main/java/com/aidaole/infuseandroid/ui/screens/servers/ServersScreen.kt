package com.aidaole.infuseandroid.ui.screens.servers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aidaole.infuseandroid.domain.model.ServerInfo
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun ServersScreen(
    navController: NavController,
    viewModel: ServersViewModel = hiltViewModel()
) {
    val savedServers by viewModel.savedServers.collectAsState()
    val discoveredServers = viewModel.discoveredServers
    val isDiscovering = viewModel.isDiscovering
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedServer by remember { mutableStateOf<ServerInfo?>(null) }
    
    // 直接连接服务器
    var directConnectIp by remember { mutableStateOf("") }
    var directConnectUsername by remember { mutableStateOf("") }
    var directConnectPassword by remember { mutableStateOf("") }
    
    // 显示错误消息
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.clearError()
            }
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "添加服务器"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "SMB服务器管理",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 直接连接区域
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "直接连接",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    OutlinedTextField(
                        value = directConnectIp,
                        onValueChange = { directConnectIp = it },
                        label = { Text("服务器IP地址") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = directConnectUsername,
                            onValueChange = { directConnectUsername = it },
                            label = { Text("用户名 (可选)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        
                        OutlinedTextField(
                            value = directConnectPassword,
                            onValueChange = { directConnectPassword = it },
                            label = { Text("密码 (可选)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = {
                            if (directConnectIp.isNotBlank()) {
                                val serverInfo = ServerInfo(
                                    id = "",
                                    name = "SMB服务器 (${directConnectIp})",
                                    host = directConnectIp,
                                    port = 445,
                                    username = directConnectUsername.takeIf { it.isNotBlank() },
                                    password = directConnectPassword.takeIf { it.isNotBlank() },
                                    isActive = true
                                )
                                
                                // 先测试连接
                                viewModel.testConnection(serverInfo) { success ->
                                    scope.launch {
                                        if (success) {
                                            // 连接成功后保存
                                            viewModel.saveServer(serverInfo)
                                            snackbarHostState.showSnackbar("连接成功并已保存")
                                            // 清空输入
                                            directConnectIp = ""
                                            directConnectUsername = ""
                                            directConnectPassword = ""
                                        } else {
                                            snackbarHostState.showSnackbar("连接失败，请检查信息")
                                        }
                                    }
                                }
                            }
                        },
                        enabled = directConnectIp.isNotBlank() && !isLoading,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("连接并保存")
                    }
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "已保存的服务器",
                    style = MaterialTheme.typography.titleMedium
                )
                
                IconButton(
                    onClick = { viewModel.discoverServers() },
                    enabled = !isDiscovering && !isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "扫描局域网"
                    )
                }
            }
            
            if (isDiscovering) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("正在扫描局域网中的SMB服务器...")
                    }
                }
            }
            
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (savedServers.isNotEmpty()) {
                    items(savedServers) { server ->
                        ServerItem(
                            server = server,
                            onDelete = { viewModel.deleteServer(it) },
                            onEdit = { selectedServer = it },
                            onTest = { viewModel.testConnection(it) { success ->
                                scope.launch {
                                    val message = if (success) "连接成功" else "连接失败"
                                    snackbarHostState.showSnackbar(message)
                                }
                            }}
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                } else {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("没有已保存的服务器，点击右下角的按钮添加")
                        }
                    }
                }
            }
            
            if (discoveredServers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "发现的服务器",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(discoveredServers) { server ->
                        DiscoveredServerItem(
                            server = server,
                            onSave = { viewModel.saveServer(it) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
    
    // 添加/编辑服务器对话框
    if (showAddDialog || selectedServer != null) {
        ServerDialog(
            server = selectedServer,
            onDismiss = {
                showAddDialog = false
                selectedServer = null
            },
            onSave = { server ->
                viewModel.saveServer(server)
                showAddDialog = false
                selectedServer = null
            }
        )
    }
}

@Composable
fun ServerItem(
    server: ServerInfo,
    onDelete: (ServerInfo) -> Unit,
    onEdit: (ServerInfo) -> Unit,
    onTest: (ServerInfo) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit(server) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = server.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${server.host}:${server.port}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { onTest(server) }
                ) {
                    Text("测试连接")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { onDelete(server) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun DiscoveredServerItem(
    server: ServerInfo,
    onSave: (ServerInfo) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = server.name,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = server.host,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        IconButton(
            onClick = { onSave(server) }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "保存",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ServerDialog(
    server: ServerInfo?,
    onDismiss: () -> Unit,
    onSave: (ServerInfo) -> Unit
) {
    val isEditing = server != null
    var name by remember { mutableStateOf(server?.name ?: "") }
    var host by remember { mutableStateOf(server?.host ?: "") }
    var port by remember { mutableStateOf(server?.port?.toString() ?: "445") }
    var username by remember { mutableStateOf(server?.username ?: "") }
    var password by remember { mutableStateOf(server?.password ?: "") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isEditing) "编辑服务器" else "添加服务器")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("服务器名称") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = host,
                    onValueChange = { host = it },
                    label = { Text("主机地址 (IP)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text("端口") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("用户名 (可选)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("密码 (可选)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val portInt = port.toIntOrNull() ?: 445
                    val newServer = ServerInfo(
                        id = server?.id ?: "",
                        name = name,
                        host = host,
                        port = portInt,
                        username = username.takeIf { it.isNotBlank() },
                        password = password.takeIf { it.isNotBlank() },
                        isActive = true
                    )
                    onSave(newServer)
                },
                enabled = name.isNotBlank() && host.isNotBlank()
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
} 