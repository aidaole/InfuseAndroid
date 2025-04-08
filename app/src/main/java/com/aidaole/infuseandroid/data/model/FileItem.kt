package com.aidaole.infuseandroid.data.model

sealed class FileItem {
    data class File(
        val name: String,
        val path: String,
        val size: Long,
        val lastModified: Long,
        val isVideo: Boolean = false
    ) : FileItem()

    data class Directory(
        val name: String,
        val path: String,
        val lastModified: Long,
        val children: List<FileItem> = emptyList()
    ) : FileItem()
} 