package com.aidaole.infuseandroid.repository

import com.aidaole.infuseandroid.domain.model.FileItem
import com.aidaole.infuseandroid.domain.model.SmbServer
import com.aidaole.infuseandroid.domain.repository.SmbRepository
import com.hierynomus.msfscc.FileAttributes
import com.hierynomus.smbj.SMBClient
import com.hierynomus.smbj.auth.AuthenticationContext
import com.hierynomus.smbj.connection.Connection
import com.hierynomus.smbj.session.Session
import com.hierynomus.smbj.share.DiskShare
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class SmbRepositoryImpl @Inject constructor() : SmbRepository {
    private val servers = MutableStateFlow<List<SmbServer>>(
        listOf(SmbServer("1", "mac", "192.168.31.191", "yxm", "xiao", false))

    )
    private val connections = mutableMapOf<String, Connection>()
    private val sessions = mutableMapOf<String, Session>()

    override fun getServers(): Flow<List<SmbServer>> = servers

    override suspend fun addServer(server: SmbServer) {
        val currentList = servers.value.toMutableList()
        val newServer = server.copy(id = UUID.randomUUID().toString())
        currentList.add(newServer)
        servers.value = currentList
    }

    override suspend fun removeServer(serverId: String) {
        val currentList = servers.value.toMutableList()
        currentList.removeAll { it.id == serverId }
        servers.value = currentList
        disconnectFromServer(serverId)
    }

    override suspend fun connectToServer(server: SmbServer): Boolean = withContext(Dispatchers.IO) {
        try {
            val client = SMBClient()
            val authContext = AuthenticationContext(server.username, server.password.toCharArray(), server.host)
            val connection = client.connect(server.host)
            val session = connection.authenticate(authContext)

            if (session != null) {
                connections[server.id] = connection
                sessions[server.id] = session
                val updatedServer = server.copy(isConnected = true)
                val currentList = servers.value.toMutableList()
                val index = currentList.indexOfFirst { it.id == server.id }
                if (index != -1) {
                    currentList[index] = updatedServer
//                    scanDirectory(updatedServer.host, "/")
                    servers.value = currentList
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun disconnectFromServer(serverId: String) {
        sessions[serverId]?.close()
        connections[serverId]?.close()
        sessions.remove(serverId)
        connections.remove(serverId)

        val currentList = servers.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == serverId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isConnected = false)
            servers.value = currentList
        }
    }

    private suspend fun listShares(serverId: String): List<String> = withContext(Dispatchers.IO) {
        val session = sessions[serverId] ?: throw IllegalStateException("Server not connected")
        try {
            session.connectShare("IPC$").use { ipcShare ->
                listOf(ipcShare.smbPath.shareName)
            }
//            session.connection.connectShare("IPC$").use { ipcShare ->
//                session.connection.listShares().map { it.name }
//            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun scanDirectory(serverId: String, path: String): List<FileItem> = withContext(Dispatchers.IO) {
        val session = sessions[serverId] ?: throw IllegalStateException("Server not connected")
        
        if (path == "/") {
            return@withContext listShares(serverId).map { shareName ->
                FileItem.Directory(
                    name = shareName,
                    path = "/$shareName",
                    lastModified = System.currentTimeMillis()
                )
            }
        }

        val shareName = path.split("/").filter { it.isNotEmpty() }.firstOrNull()
            ?: throw IllegalStateException("Invalid path")
        val relativePath = path.substringAfter("/$shareName").let {
            if (it.isEmpty()) "" else it.substring(1)
        }

        val share = session.connectShare(shareName) as? DiskShare
            ?: throw IllegalStateException("Failed to connect to share")

        try {
            val items = mutableListOf<FileItem>()
            share.list(relativePath).forEach { entry ->
                val fullPath = "$path/${entry.fileName}"
                if (entry.fileAttributes == FileAttributes.FILE_ATTRIBUTE_DIRECTORY.value) {
                    items.add(FileItem.Directory(
                        name = entry.fileName,
                        path = fullPath,
                        lastModified = entry.changeTime.toEpochMillis()
                    ))
                } else {
                    items.add(FileItem.File(
                        name = entry.fileName,
                        path = fullPath,
                        size = entry.endOfFile,
                        lastModified = entry.changeTime.toEpochMillis(),
                        isVideo = isVideoFile(entry.fileName)
                    ))
                }
            }
            items
        } finally {
            share.close()
        }
    }

    private fun isVideoFile(fileName: String): Boolean {
        val videoExtensions = listOf(
            ".mp4", ".mkv", ".avi", ".mov", ".wmv", ".flv", ".webm",
            ".m4v", ".mpg", ".mpeg", ".3gp", ".3g2", ".ts", ".mts"
        )
        return videoExtensions.any { fileName.lowercase().endsWith(it) }
    }
} 