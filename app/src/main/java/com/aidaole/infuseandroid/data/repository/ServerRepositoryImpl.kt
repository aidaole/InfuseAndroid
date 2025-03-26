package com.aidaole.infuseandroid.data.repository

import android.content.Context
import com.aidaole.infuseandroid.data.datasource.dao.ServerDao
import com.aidaole.infuseandroid.data.model.ServerEntity
import com.aidaole.infuseandroid.domain.model.ServerInfo
import com.aidaole.infuseandroid.domain.util.NetworkUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import jcifs.CIFSContext
import jcifs.CIFSException
import jcifs.context.SingletonContext
import jcifs.smb.NtlmPasswordAuthenticator
import jcifs.smb.SmbFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetAddress
import java.net.UnknownHostException
import javax.inject.Inject

class ServerRepositoryImpl @Inject constructor(
    private val serverDao: ServerDao,
    private val networkUtils: NetworkUtils,
    @ApplicationContext private val context: Context
) : ServerRepository {

    override fun getAllServers(): Flow<List<ServerInfo>> {
        return serverDao.getAllServers().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getServerById(id: String): ServerInfo? {
        return serverDao.getServerById(id)?.toDomain()
    }

    override suspend fun getServerByHost(host: String): ServerInfo? {
        return serverDao.getServerByHost(host)?.toDomain()
    }

    override suspend fun saveServer(server: ServerInfo) {
        serverDao.insertServer(server.toEntity())
    }

    override suspend fun updateServer(server: ServerInfo) {
        serverDao.updateServer(server.toEntity())
    }

    override suspend fun deleteServer(server: ServerInfo) {
        serverDao.deleteServer(server.toEntity())
    }

    override suspend fun updateLastConnected(id: String, timestamp: Long) {
        serverDao.updateLastConnected(id, timestamp)
    }

    override suspend fun updateFavorite(id: String, isFavorite: Boolean) {
        serverDao.updateFavorite(id, isFavorite)
    }

    override suspend fun discoverServers(): List<ServerInfo> = withContext(Dispatchers.IO) {
        val servers = mutableListOf<ServerInfo>()
        try {
            // 获取本地IP地址的前缀部分，例如192.168.1.xxx
            val localIp = networkUtils.getLocalIpPrefix()
            if (localIp != null) {
                for (i in 1..254) {
                    val host = "$localIp$i"
                    if (isHostReachable(host)) {
                        try {
                            val hostname = InetAddress.getByName(host).hostName
                            val name = if (hostname != host) hostname else "未知服务器"
                            servers.add(
                                ServerInfo(
                                    id = "",
                                    name = name,
                                    host = host,
                                    port = 445,
                                    isActive = true
                                )
                            )
                        } catch (e: UnknownHostException) {
                            // 无法解析主机名，使用IP作为名称
                            servers.add(
                                ServerInfo(
                                    id = "",
                                    name = "SMB服务器",
                                    host = host,
                                    port = 445,
                                    isActive = true
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        servers
    }

    override suspend fun testConnection(server: ServerInfo): Boolean = withContext(Dispatchers.IO) {
        try {
            val cifsContext = createCifsContext(server)
            val smbFile = SmbFile("smb://${server.host}/", cifsContext)
            smbFile.exists()
            true
        } catch (e: CIFSException) {
            e.printStackTrace()
            false
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun createCifsContext(server: ServerInfo): CIFSContext {
        val cifsContext = SingletonContext.getInstance()
        return if (server.username != null && server.password != null) {
            val auth = NtlmPasswordAuthenticator(server.username, server.password)
            cifsContext.withCredentials(auth)
        } else {
            cifsContext.withGuestCrendentials()
        }
    }

    private fun isHostReachable(host: String): Boolean {
        return try {
            val address = InetAddress.getByName(host)
            address.isReachable(1000) // 1秒超时
        } catch (e: Exception) {
            false
        }
    }

    private fun ServerEntity.toDomain(): ServerInfo {
        return ServerInfo(
            id = id,
            name = name,
            host = host,
            port = port,
            username = username,
            password = password,
            isActive = isActive
        )
    }

    private fun ServerInfo.toEntity(): ServerEntity {
        return ServerEntity(
            id = id.ifEmpty { ServerEntity(
                name = "",
                host = "",
                port = 0,
                username = null,
                password = null,
            ).id },
            name = name,
            host = host,
            port = port,
            username = username,
            password = password,
            isActive = isActive
        )
    }
} 