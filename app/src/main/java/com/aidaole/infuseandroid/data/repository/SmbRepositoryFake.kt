package com.aidaole.infuseandroid.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.aidaole.infuseandroid.data.model.FileItem
import com.aidaole.infuseandroid.data.model.SmbServer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class SmbRepositoryFake @Inject constructor() : SmbRepository {

    private val servers = mutableListOf<SmbServer>()

    override fun getServers(): Flow<List<SmbServer>> {
        return flow {
            emit(
                listOf(
                    SmbServer("0", "mac", "192.168.1.1", "y", "x", true)
                )
            )
        }
    }

    override suspend fun addServer(server: SmbServer) {
        servers.add(server)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun removeServer(serverId: String) {
        servers.removeIf {
            it.id == serverId
        }
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