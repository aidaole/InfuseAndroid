package com.aidaole.infuseandroid.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.aidaole.infuseandroid.data.model.FileItem
import com.aidaole.infuseandroid.data.model.SmbServer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class SmbRepositoryFake @Inject constructor() : SmbRepository {

    private val _servers = MutableStateFlow<List<SmbServer>>(emptyList())
    private val servers: Flow<List<SmbServer>> = _servers.asStateFlow()

    override fun getServers(): Flow<List<SmbServer>> {
        return servers
    }

    override suspend fun addServer(server: SmbServer) {
        _servers.value += server
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun removeServer(serverId: String) {
        _servers.value = _servers.value.filter { it.id != serverId }
    }

    override suspend fun connectToServer(server: SmbServer): Boolean {
        delay(1000)
        return true
    }

    override suspend fun disconnectFromServer(serverId: String) {

    }

    override suspend fun scanDirectory(serverId: String, path: String): List<FileItem> {
        return listOf<FileItem>(
            FileItem.Directory("root", ".", System.currentTimeMillis(), emptyList())
        )
    }
}