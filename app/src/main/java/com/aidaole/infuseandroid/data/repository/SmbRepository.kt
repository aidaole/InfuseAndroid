package com.aidaole.infuseandroid.data.repository

import com.aidaole.infuseandroid.data.model.FileItem
import com.aidaole.infuseandroid.data.model.SmbServer
import kotlinx.coroutines.flow.Flow

interface SmbRepository {
    fun getServers(): Flow<List<SmbServer>>
    suspend fun addServer(server: SmbServer)
    suspend fun removeServer(serverId: String)
    suspend fun connectToServer(server: SmbServer): Boolean
    suspend fun disconnectFromServer(serverId: String)
    
    // 新增扫描文件相关方法
    suspend fun scanDirectory(serverId: String, path: String): List<FileItem>
}