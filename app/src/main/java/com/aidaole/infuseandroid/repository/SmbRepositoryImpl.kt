package com.aidaole.infuseandroid.repository

import android.util.Log
import com.aidaole.infuseandroid.domain.model.FileItem
import com.aidaole.infuseandroid.domain.model.SmbServer
import com.aidaole.infuseandroid.domain.repository.SmbRepository
import jcifs.CIFSContext
import jcifs.config.BaseConfiguration
import jcifs.context.BaseContext
import jcifs.smb.NtlmPasswordAuthentication
import jcifs.smb.SmbFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject


class SmbRepositoryImpl @Inject constructor() : SmbRepository {
    companion object {
        private const val TAG = "SmbRepositoryImpl"
    }

    private val servers = MutableStateFlow<List<SmbServer>>(
        listOf(SmbServer("1", "mac", "192.168.31.191", "yxm", "xiao", false))
    )
    private val connections = mutableMapOf<String, CIFSContext>()

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
            val baseContext = BaseContext(BaseConfiguration(true))
            val auth = NtlmPasswordAuthentication(
                baseContext,
                server.host,
                server.username,
                server.password
            )
            val smbContext = baseContext.withCredentials(auth)

            val smbUrl = "smb://${server.host}"
            val smbFile = SmbFile(smbUrl, smbContext)

            // 测试连接是否成功
            smbFile.exists()

            connections[server.id] = smbContext
            val updatedServer = server.copy(isConnected = true)
            val currentList = servers.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == server.id }
            if (index != -1) {
                currentList[index] = updatedServer
                servers.value = currentList
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun disconnectFromServer(serverId: String) {
        connections.remove(serverId)
        val currentList = servers.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == serverId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isConnected = false)
            servers.value = currentList
        }
    }

    override suspend fun scanDirectory(serverId: String, path: String): List<FileItem> =
        withContext(Dispatchers.IO) {
            try {
                val auth = connections[serverId]
                val host = servers.value.find { it.id == serverId }?.host
                val smbFile = SmbFile("smb://${host}/$path", auth)
                smbFile.listFiles()?.map { file ->
                    if (file.isDirectory) {
                        FileItem.Directory(
                            name = file.name.removeSuffix("/"),
                            path = file.path.removePrefix("smb://$host/"),
                            lastModified = file.lastModified()
                        )
                    } else {
                        FileItem.File(
                            name = file.name,
                            path = file.path.removePrefix("smb://$host/"),
                            size = file.length(),
                            lastModified = file.lastModified(),
                            isVideo = isVideoFile(file.name)
                        )
                    }
                } ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
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