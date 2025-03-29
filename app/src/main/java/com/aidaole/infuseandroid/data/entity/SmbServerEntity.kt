package com.aidaole.infuseandroid.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aidaole.infuseandroid.domain.model.SmbServer

@Entity(tableName = "smb_servers")
data class SmbServerEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val host: String,
    val username: String,
    val password: String,
    val isConnected: Boolean = false
)

// 添加扩展函数用于Entity和Domain模型之间的转换
fun SmbServerEntity.toDomain() = SmbServer(
    id = id,
    name = name,
    host = host,
    username = username,
    password = password,
    isConnected = isConnected
)

fun SmbServer.toEntity() = SmbServerEntity(
    id = id,
    name = name,
    host = host,
    username = username,
    password = password,
    isConnected = isConnected
) 