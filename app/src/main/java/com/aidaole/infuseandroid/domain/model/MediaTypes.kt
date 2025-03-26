package com.aidaole.infuseandroid.domain.model

import java.util.Date

/**
 * 服务器信息
 */
data class ServerInfo(
    val id: String,
    val name: String,
    val host: String,
    val port: Int = 445,
    val username: String? = null,
    val password: String? = null,
    val isActive: Boolean = true
)

/**
 * 电影信息
 */
data class Movie(
    val id: String,
    val title: String,
    val path: String,
    val posterUrl: String? = null,
    val year: Int? = null,
    val overview: String? = null,
    val duration: Long? = null,
    val serverId: String,
    val lastPlayPosition: Long = 0,
    val addedDate: Date = Date()
)

/**
 * 电视剧信息
 */
data class TvShow(
    val id: String,
    val title: String,
    val folderPath: String,
    val posterUrl: String? = null,
    val year: Int? = null,
    val overview: String? = null,
    val seasons: List<Season> = emptyList(),
    val serverId: String,
    val addedDate: Date = Date()
)

/**
 * 电视剧季信息
 */
data class Season(
    val id: String,
    val seasonNumber: Int,
    val episodes: List<Episode> = emptyList()
)

/**
 * 电视剧集信息
 */
data class Episode(
    val id: String,
    val title: String,
    val episodeNumber: Int,
    val path: String,
    val duration: Long? = null,
    val lastPlayPosition: Long = 0
)

/**
 * 视频文件信息
 */
data class VideoFileInfo(
    val name: String,
    val path: String,
    val size: Long,
    val isDirectory: Boolean,
    val lastModified: Date,
    val serverId: String
) 