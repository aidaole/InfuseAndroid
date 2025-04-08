package com.aidaole.infuseandroid.data.repository


import com.aidaole.infuseandroid.data.dao.SmbServerDao
import com.aidaole.infuseandroid.data.entity.toDomain
import com.aidaole.infuseandroid.data.entity.toEntity
import com.aidaole.infuseandroid.data.model.FileItem
import com.aidaole.infuseandroid.data.model.SmbServer
import jcifs.CIFSContext
import jcifs.config.BaseConfiguration
import jcifs.context.BaseContext
import jcifs.smb.NtlmPasswordAuthentication
import jcifs.smb.SmbFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class SmbRepositoryImpl @Inject constructor(
    private val smbServerDao: SmbServerDao
) : SmbRepository {
    companion object {
        private const val TAG = "SmbRepositoryImpl"
    }

    private val connections = mutableMapOf<String, CIFSContext>()

    override fun getServers(): Flow<List<SmbServer>> =
        smbServerDao.getAllServers().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun addServer(server: SmbServer) {
        val newServer = server.copy(id = UUID.randomUUID().toString())
        smbServerDao.insertServer(newServer.toEntity())
    }

    override suspend fun removeServer(serverId: String) {
        smbServerDao.deleteServer(serverId)
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
            smbServerDao.updateServerConnection(server.id, true)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun disconnectFromServer(serverId: String) {
        connections.remove(serverId)
        smbServerDao.updateServerConnection(serverId, false)
    }

    override suspend fun scanDirectory(serverId: String, path: String): List<FileItem> =
        withContext(Dispatchers.IO) {
            try {
                val auth = connections[serverId]
                val host = smbServerDao.findServer(serverId).host
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