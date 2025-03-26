package com.aidaole.infuseandroid.data.repository

import com.aidaole.infuseandroid.data.model.ServerEntity
import com.aidaole.infuseandroid.domain.model.ServerInfo
import kotlinx.coroutines.flow.Flow

interface ServerRepository {
    fun getAllServers(): Flow<List<ServerInfo>>
    suspend fun getServerById(id: String): ServerInfo?
    suspend fun getServerByHost(host: String): ServerInfo?
    suspend fun saveServer(server: ServerInfo)
    suspend fun updateServer(server: ServerInfo)
    suspend fun deleteServer(server: ServerInfo)
    suspend fun updateLastConnected(id: String, timestamp: Long)
    suspend fun updateFavorite(id: String, isFavorite: Boolean)
    suspend fun discoverServers(): List<ServerInfo>
    suspend fun testConnection(server: ServerInfo): Boolean
} 