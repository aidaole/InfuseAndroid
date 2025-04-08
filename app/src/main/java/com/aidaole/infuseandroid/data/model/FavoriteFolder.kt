package com.aidaole.infuseandroid.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_folders")
data class FavoriteFolder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val serverId: String,
    val path: String,
    val name: String,
    val lastModified: Long = System.currentTimeMillis()
) 