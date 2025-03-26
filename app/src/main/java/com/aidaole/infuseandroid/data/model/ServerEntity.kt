package com.aidaole.infuseandroid.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "servers")
data class ServerEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val host: String,
    val port: Int = 445,
    val username: String? = null,
    val password: String? = null,
    val isActive: Boolean = true,
    val isFavorite: Boolean = false,
    val lastConnected: Long = 0
) 