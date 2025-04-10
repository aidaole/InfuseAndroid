package com.aidaole.infuseandroid.data.model

class FavoriteFolder(
    val id: Long = 0,
    val serverId: String,
    val path: String,
    val name: String,
    val lastModified: Long = System.currentTimeMillis()
)