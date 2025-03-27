package com.aidaole.infuseandroid.repository

import com.aidaole.infuseandroid.domain.model.SmbServer
import com.aidaole.infuseandroid.domain.repository.SmbRepository
import com.hierynomus.smbj.SMBClient
import com.hierynomus.smbj.auth.AuthenticationContext
import com.hierynomus.smbj.connection.Connection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import javax.inject.Inject

class SmbRepositoryImpl @Inject constructor() : SmbRepository {
    private val servers = MutableStateFlow<List<SmbServer>>(emptyList())
    private val connections = mutableMapOf<String, Connection>()

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

    override suspend fun connectToServer(server: SmbServer): Boolean {
        return try {
            val client = SMBClient()
            val authContext = AuthenticationContext(server.username, server.password.toCharArray(), server.host)
            val connection = client.connect(server.host)
            val session = connection.authenticate(authContext)
            
            if (session != null) {
                connections[server.id] = connection
                val updatedServer = server.copy(isConnected = true)
                val currentList = servers.value.toMutableList()
                val index = currentList.indexOfFirst { it.id == server.id }
                if (index != -1) {
                    currentList[index] = updatedServer
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
        connections[serverId]?.close()
        connections.remove(serverId)
        
        val currentList = servers.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == serverId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isConnected = false)
            servers.value = currentList
        }
    }
} 